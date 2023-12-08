import re

#function - 문자열에서 숫자들 골라서 리스트에 넣기
def get_list_of_digit_in_string(string):
    numbers = re.findall(r'\d+', string) # r : 이스케이프를 특문으로 인식, \d : digit, + : 앞의 패턴이 여러 번 나타날 것을 고려, findall() : 찾은 모든 것을 리스트로 반환
    return list(map(int, numbers))

#function - 문자열에 들어간 공백, 개행, 탭 없애기
def delete_whitespace(string):
    return re.sub(r'\s+', '', string) # \s : whitespace, sub() : 찾은걸 두 번째 인자로 대체하고 반환

#function - 구간 크기가 range_size가 되도록 나누어 반환 0 ~ length-1
def divide_range_by_size(length, range_size):
    result = []
    current = 0
    while current <= length - 1:
        result.append((current, min(current + range_size - 1, length - 1)))
        current += range_size
    return result

#function - 구간이 range_number개가 되도록 나누어 반환 0 ~ length-1
def divide_range_by_number(length, range_number):
    result = []
    current = 0
    for i in range(range_number):
        end = current + (length // range_number) - 1 + (1 if (length % range_number) > i else 0)
        result.append((current, end))
        current = end + 1
    return result