import nest_asyncio
import asyncio
import sys
import json
from modules.config.crawler_config import (
    URL, 
    YEAR_DD_ATT,
    TERM_DD_ATT,
    GRADE_DD_ATT, 
    COURSE_NAME_INPUT_ATT, 
    COURSE_TABLE_ATT, 
    SEARCH_BTN_ATT,)
from modules.function.util_function import delete_whitespace
from modules.function.crawling_function import (
    create_browser,
    create_new_pages,
    clickElementWithWait,
    typeToElementWithWait,
    set_dropdown_by_index,
    get_tabledata,
)


async def main():
    browser = await create_browser(False)
    pages = await create_new_pages(browser, URL, 1)
    
    await set_dropdown_by_index(pages[0], YEAR_DD_ATT, "1") #임시
    await set_dropdown_by_index(pages[0], TERM_DD_ATT, "2") #임시

    while True:
        line = sys.stdin.readline()
        if not line:
            continue
        dict_data = json.loads(line)

        course_name = dict_data['name']
        course_code = dict_data['code']
        course_grade = dict_data['grade']

        try:
            #찾았나요?
            found = 0

            #드랍다운 선택
            await set_dropdown_by_index(pages[0], GRADE_DD_ATT, course_grade)
            
            #여석 가져올 교과목명 입력
            await typeToElementWithWait(pages[0], COURSE_NAME_INPUT_ATT, course_name)#course_name
            
            #조회 버튼 클릭
            await clickElementWithWait(pages[0], SEARCH_BTN_ATT)
            
            #테이블 데이터 가져오기
            courses = await get_tabledata(pages[0], COURSE_TABLE_ATT)
            if courses == -1:
                print(json.dumps({'errorType': 3}))
                sys.stdout.flush()
                continue
            if len(courses) == 1:
                print(json.dumps({'errorType': 2}))
                sys.stdout.flush()
                continue

            #강의 중 내가 원하는 과목인지 확인하기 위해 반복
            for course in courses :
                #빈 행 넘김
                if not len(course) :
                    continue
                #선택한 행이 내가 원하는 과목인지 체크 후 진행
                if delete_whitespace(course[2]) == course_code :
                    found = 1
                    print(json.dumps({'errorType': 0, 'name': course[1]}))
                    sys.stdout.flush()
                    break
            
            #강의 없을 때
            if found == 0:
                print(json.dumps({'errorType': 2}))
                sys.stdout.flush()

        except:
            print(json.dumps({'errorType': 3})) 
            sys.stdout.flush()


nest_asyncio.apply()
asyncio.run(main())