import nest_asyncio
import asyncio
import time

nest_asyncio.apply()

#부모에서는 쓰지 않을거라 선언만 해둠
def worker():
    pass



#parent process --------------------------------------------------------------------
if __name__ == "__main__":
    #import sys
    from math import ceil
    from modules.function.mp_function import *
    from modules.config.mp_config import *
    from modules.function.util_function import (
        divide_range_by_number,
        divide_range_by_size)
    from modules.database.mysql_database import create_connection

    #프로세스 초기화
    alive_proc_num = 0 #실행 중인 프로세스 개수
    unused_count = [0] * PROCESS_NUMBER # 프로세스가 사용되지 않은 횟수
    parent_pipes, child_pipes = create_pipes(PROCESS_NUMBER)
    procs = [] #프로세스 리스트
    for i in range(PROCESS_NUMBER):
        proc = create_process(worker, (child_pipes[i],), i)
        procs.append(proc)

    #자식이 켜질 때까지 기다림
    time.sleep(10)

    restart_child = False #자식 프로세스를 종료하고 다시 시작할지 나타냄 - 오류라고 판단될 경우 설정함

    #찾을 과목 가져오고, 자식들에게 나눠서 보내고, 자식들의 작업이 끝나면 다시 반복
    while True:
        connection = create_connection()
        cursor = connection.cursor()
        start_time_for_one_cycle = time.time()

        print('----------------사이클 시작----------------')

        #자식 프로세스 재시작할지?
        if restart_child == True:
            unused_count = [0] * PROCESS_NUMBER # 프로세스가 사용되지 않은 횟수 초기화
            for i in range(PROCESS_NUMBER): #프로세스 종료
                close_process(procs[i], parent_pipes[i], i) #동기
                procs[i] = create_process(worker, (child_pipes[i],), i) #프로세스 시작 후 프로세스 객체 다시 할당
            restart_child = False
            #자식이 켜질 때까지 기다림
            time.sleep(10)
        #찾아야 할 과목 가져오고, 과목 개수 얻고, 과목 개수에 따라 자식을 기다릴 기한 설정
        cursor.execute("""SELECT code, name, grade, is_self_alerted, is_other_alerted, id
        FROM course c
        WHERE EXISTS (
            SELECT 1
            FROM applicant a
            WHERE a.course_id = c.id
        )""")

        courses = cursor.fetchall() # 0:code, 1: name, 2: grade, 3: is_self_alerted, 4: is_other_alerted, 5: id

        courses_num = len(courses)
        cursor.close()
        connection.close()

        #살아있는 프로세스 개수
        alive_proc_num = sum(1 for i in range(PROCESS_NUMBER) if procs[i].is_alive())

        print(f'등록된 강의 수: {courses_num}')
        print(f'살아있는 프로세스 수: {alive_proc_num}')

        #신청 과목 없을 땐 살아있는 프로세스도 없어야 함. 둘 다 0이면 다음 로직을 실행하지 않고 과목 수만 체크할 수 있도록 continue 실행
        if not courses_num and not alive_proc_num:
            time.sleep(10)
            continue

        deadline_to_wait_child = courses_num * DEADLINE_TO_FIND_ONE_COURSE #가중치 설정해줘야 할 수도 있음

        #courses_ranges : 자식들에게 각각 전달할 범위 설정
        if courses_num < PROCESS_NUMBER * PAGE_NUMBER: 
            courses_ranges = divide_range_by_size(courses_num, PAGE_NUMBER) #생일 빠른 프로세스에 검색할거 몰아넣기
        else: #실행 중인 프로세스 개수만큼 범위 설정
            courses_ranges = divide_range_by_number(courses_num, alive_proc_num)
        
        #파이프 - 발신
        needed_proc_num = min(ceil(courses_num/PAGE_NUMBER), PROCESS_NUMBER) #필요한 프로세스 수
        sended_data_to_proc_num = 0 #과목을 정상적으로 보낸 자식의 개수
        for i, pipe in enumerate(parent_pipes):
            if needed_proc_num <= i: #필요 없는 프로세스일 경우
                if unused_count[i] == COUNT_TO_CLOSE_PROC: #일정 횟수 이상 사용되지 않았을 때
                    if alive_proc_num > i: #프로세스 살아있을 때
                        close_process(procs[i], pipe, i)
                else:
                    unused_count[i] += 1
                    push_and_flush_stdout('log', f'proc{i} inc:{unused_count[i]}')
                continue #필요 없는 프로세스이므로
            else: #필요한 프로세스일 경우
                if alive_proc_num <= i: #실행 중이지 않을 때 - 실행 중이지 않을 때 기준으로 범위를 만들었는데, 여기서 확인해보니 실행 중이어도 범위의 개수는 더 작아서 괜찮음.
                    if unused_count[i] == 0: #일정 횟수 이상 필요로 할 때
                        procs[i] = create_process(worker, (child_pipes[i],), i) #생성됨이 먼저 뜨고 페이지가 켜지는지 확인
                    else: 
                        unused_count[i] -= 1
                        push_and_flush_stdout('log', f'proc{i} dec:{unused_count[i]}')
                    continue #실행이 완료될 다음을 기약하며...
            
            #보낼 과목 준비
            start, end = courses_ranges[i]
            data = ['crawl', courses[start:end+1]]

            #보내기
            try:
                pipe.send(data)
                sended_data_to_proc_num += 1
                print(f'{i}번 프로세스에게 등록된 강의({start}~{end}) 전송')
                # push_and_flush_stdout('log', f'parent to child{i} : sended')
            except Exception as error:
                push_and_flush_stdout('log', f'parent to child{i} : error : {error}') # 경우1 : 프로세스 퍼져서 파이프 닫혔을 때
        
        print(f'데드라인: {deadline_to_wait_child}초')
        #파이프 수신 - 자식 프로세스들이 완료될 때까지 기다림
        received_data_num = 0
        start_time_to_wait_child = time.time()
        while True:
            time.sleep(0.1)
            for i in range(sended_data_to_proc_num):
                if parent_pipes[i].poll():
                    received_data = parent_pipes[i].recv()
                    received_data_num += 1
            if received_data_num == sended_data_to_proc_num:
                break
            if (time.time() - start_time_to_wait_child) > deadline_to_wait_child:
                restart_child = True
                push_and_flush_stdout('log', f"timeover : failed to wait child")
                break

        print(f'모든 강의 여석 확인에 걸린 시간: {round(time.time() - start_time_for_one_cycle, 3)}s')
        # push_and_flush_stdout('log', f'wait_child: {round(time.time() - start_time_to_wait_child, 3)}s')
        # push_and_flush_stdout('log', f'one_cycle: {round(time.time() - start_time_for_one_cycle, 3)}s')


#child process --------------------------------------------------------------------
else:
    from modules.function.mp_function import push_and_flush_stdout
    from modules.config.mp_config import PAGE_NUMBER
    from modules.config.crawler_config import (
        URL, 
        COURSE_NAME_INPUT_ATT,
        YEAR_DD_ATT,
        TERM_DD_ATT,
        GRADE_DD_ATT,
        SEARCH_BTN_ATT,
        COURSE_TABLE_ATT)
    from modules.function.crawling_function import (
        create_browser,
        create_new_pages,
        close_browser,
        clickElementWithWait,
        typeToElementWithWait,
        set_dropdown_by_index,
        get_tabledata)
    from modules.function.util_function import (
        get_list_of_digit_in_string,
        delete_whitespace,
        divide_range_by_size,
        divide_range_by_number)
    from modules.function.push_notification import send_push_notification
    from modules.database.mysql_database import create_connection_pool, create_connection

    #여석 체크
    async def check_seat(page, courses_to_find):
        mutated_courses = []
        for course_to_find in courses_to_find:
            #드랍다운 선택
            await set_dropdown_by_index(page, YEAR_DD_ATT, "1") #임시
            await set_dropdown_by_index(page, TERM_DD_ATT, "2")
            await set_dropdown_by_index(page, GRADE_DD_ATT, course_to_find[2]) #'grade'
            
            #여석 가져올 교과목명 입력
            await typeToElementWithWait(page, COURSE_NAME_INPUT_ATT, course_to_find[1]) # 'name'
            
            #조회 버튼 클릭
            await clickElementWithWait(page, SEARCH_BTN_ATT)
            
            #테이블 데이터 가져오기
            courses = await get_tabledata(page, COURSE_TABLE_ATT)
            if courses == -1: #테이블 데이터 가져오기 실패
                continue
            if len(courses) == 1: #과목 존재하지 않을 때
                continue

            #강의 중 내가 원하는 과목인지 확인하기 위해 반복 : course는 한 수업의 데이터들을 배열로 가지고 있음
            for course in courses :
                #빈 행 넘김
                if not len(course) :
                    continue
                
                #선택한 행이 내가 원하는 과목인지 체크 후 진행
                if delete_whitespace(course[2]) == course_to_find[0] : #course[2] : 과목코드-분반
                    #여석 수 가져오기
                    nums = get_list_of_digit_in_string(course[8]) # course[8] : 여석 수를 나타내는 문자열
                    self_num = nums[0]+nums[3] #자과 여석 수
                    other_num = nums[1]+nums[4] #타과 여석 수

                    #여석 수 변동 확인
                    self_status = -1
                    other_status = -1
                    if course_to_find[3]:
                        if self_num == 0: self_status = 0
                    else:
                        if self_num != 0: self_status = 1
                    if course_to_find[4]:
                        if other_num == 0: other_status = 0
                    else:
                        if other_num != 0: other_status = 1

                    #여석 수 변동있을경우 서버에 전달할 과목에 추가
                    if self_status + other_status != -2 :
                        print(f'''    [{course_to_find[0]}] - {f"(자과 여석 {'생김' if self_status else '사라짐'})" if (self_status != -1) else ""} {f"(타과 여석 {'생김' if other_status else '사라짐'})" if (other_status != -1) else ""}''')
                        mutated_courses.extend([(course_to_find[5], course_to_find[0], self_status, other_status)])


        pool = create_connection_pool()
        connection = pool.get_connection()
        cursor = connection.cursor()

        for mutated_course in mutated_courses:
            self_status = mutated_course[2]
            other_status = mutated_course[3]
            query = f"""SELECT a.user_id, a.course_type
                    FROM course c
                    INNER JOIN applicant a ON c.id = a.course_id
                    WHERE c.id = {mutated_course[0]}"""
            cursor.execute(query)
            applicants_info = cursor.fetchall()
            for applicant_info in applicants_info:
            
                user_id = applicant_info[0]
                course_type = applicant_info[1]
                status_for_this_applicant = other_status if course_type else self_status
                if status_for_this_applicant == -1:
                    continue
                #세션 확인
                query = f"""SELECT u.push_notification_id
                    FROM user u
                    WHERE u.id = {user_id}"""
                cursor.execute(query)
                push_token = cursor.fetchone()
                cursor.reset()
                send_push_notification(push_token,
                            "여석이 생겼습니다!" if status_for_this_applicant else "여석이 사라졌습니다...",
                            mutated_course[1], {}
                )
            #상태 설정
        cursor.close()
        connection.close()

        return
        # return mutated_courses

    #자식 프로세스의 원동력
    def worker(pipe, pid):
        browser = asyncio.run(create_browser(True))
        pages = asyncio.run(create_new_pages(browser, URL, PAGE_NUMBER))

        while True:
            if pipe.poll():
                data = pipe.recv()
                command = data[0]

                if command == 'crawl':
                    courses = data[1]
                    courses_num = len(courses) #부모에게 전달받은 과목의 개수
                    
                    #각 페이지들에게 전달할 범위 설정 - 나눠진 과목 범위 : courses_ranges
                    if courses_num < PAGE_NUMBER: 
                        courses_ranges = divide_range_by_size(courses_num, 1) #앞의 페이지부터 과목 1개씩 할당
                    else:
                        courses_ranges = divide_range_by_number(courses_num, PAGE_NUMBER) #페이지 개수만큼 범위 나누기
                    
                    #구간을 이용해서 task를 설정 - for문
                    loop = asyncio.get_event_loop()
                    
                    #for문으로 각 페이지에게 넘겨줘야 할 강의들을 태스크로 만들고 실행
                    tasks = [loop.create_task(check_seat(pages[i], courses[courses_ranges[i][0]:courses_ranges[i][1]+1])) for i in range(len(courses_ranges))]
                    #각 페이지로부터 반환된 리스트가 한 리스트로 합쳐져 반환 (따라서 mutated_courses는 한 프로세스의 결과물)
                    loop.run_until_complete(asyncio.gather(*tasks))
                    # mutated_courses = loop.run_until_complete(asyncio.gather(*tasks))
                    # flattened_mutated_courses = [course for sublist in mutated_courses for course in sublist] #중첩된 리스트를 가공해서 1차? 중첩되지 않은? 리스트로 변환

                    try:
                        pipe.send(True)
                        # if len(flattened_mutated_courses):
                        #     push_and_flush_stdout('finding', flattened_mutated_courses)
                    except Exception as error:
                        ##실패하면 received_data 어떻게 처리하게 할지?
                        pipe.send(False)
                        push_and_flush_stdout('log', f'child{pid} to server - error : {error}')
                elif command == 'close':
                    asyncio.run(close_browser(browser))
                    return
