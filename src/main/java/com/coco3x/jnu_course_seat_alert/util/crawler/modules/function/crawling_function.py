from pyppeteer import launch
import asyncio
import os
from modules.function.mp_function import push_and_flush_stdout
from modules.config.crawler_config import (
    CHROME_PATH,
)

#브라우저 생성 - return browser
async def create_browser(headless=True):
    current_os = os.name
    browser = await launch(headless=headless, executablePath=(CHROME_PATH if current_os == "nt" else None))
    return browser

#새 페이지 생성 - return pages
async def create_new_pages(browser, url, number):
    for _ in range(number - 1): #브라우저 열면서 페이지 1개는 기본 생성되므로 -1
        await browser.newPage()
    pages = await browser.pages()
    for i in range(number):
        await pages[i].goto(url)
    return pages

#브라우저 닫기
async def close_browser(browser):
    try:
        await browser.close()
        await asyncio.sleep(1)
    except Exception as error:
        push_and_flush_stdout('log', f'close browser - error : {error}')

#async function - 버튼 보일 때까지 스크롤
async def scroll_element_into_viewport(page, attribute):
    await page.waitForSelector(attribute, visible= True)
    await page.evaluate(f'''document.querySelector("{attribute}").scrollIntoView();''')
    await page.waitFor(1000)
    return

#async function - 요소가 나타날 때까지 기다림
async def waitElementByAttributes(page, attribute) :
    handle = await page.waitForSelector(attribute, visible= True)
    return handle

#async function - 버튼 요소 클릭
async def clickElementWithWait(page, attribute) :
    handle = await waitElementByAttributes(page, attribute)
    #await page.evaluate("window.alert = function(){}")
    await handle.click()

#async function - 인풋 요소에 값 넣기
async def typeToElementWithWait(page, attribute, value) :
    await waitElementByAttributes(page, attribute)
    #존재하던 value 초기화 - await handle.type(value)에서 교체
    syntax = f'''() => {{
        const inputElement = document.querySelector("{attribute}");
        inputElement.value = "{value}";
    }}'''
    await page.evaluate(syntax)

#async function - 드롭다운의 옵션을 인덱스로 선택
async def set_dropdown_by_index(page, attribute, index):
    await page.waitForSelector(attribute)
    await page.evaluate(f'''
        ( ) => {{
        const dropDown = document.querySelector("{attribute}");
        dropDown.selectedIndex = {index};
        }}
        ''')

#async function - 테이블의 데이터 가져오기
async def get_tabledata(page, attribute, totalnum = 1):
    try:
        await page.waitForNavigation()
        await page.waitForFunction(f'''document.querySelectorAll("{attribute} tr").length >= {str(totalnum)}''')
        return await page.evaluate(f'''
                            const table = document.querySelector("{attribute}");
                            const rows = table.querySelectorAll('tr');
                            const rowData = [];
                            for (const row of rows) {{
                                const cells = row.querySelectorAll('td');
                                const cellData = [];
                                for (const cell of cells) {{
                                    cellData.push(cell.innerText.trim());
                                }}
                                rowData.push(cellData);
                            }}
                            rowData
                        ''')
    except:
        return -1

#async function - 전체 강의 수 불러오기
async def get_total_course_number(page, attribute):
    await page.waitForSelector(attribute)
    try:
        return str(await page.evaluate('document.querySelector("'+attribute+'").textContent'))
    except:
        return -1
    
#async function - 요소 찾고 반환
async def find_element(page, attribute):
    return await page.querySelector(attribute)
