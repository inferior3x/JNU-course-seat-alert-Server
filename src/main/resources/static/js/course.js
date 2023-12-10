//header - course adder
const nameElement = document.querySelector("#name");
const codeElement = document.querySelector("#code");
const gradeElement = document.querySelector("#grade");
const typeElement = document.querySelector("#type");
const addCourseBtnElement = document.querySelector("#add-course-btn");

//show course
const appliedCourseStatusElement = document.querySelector("#applied-course-status");
const courseSectionElement = document.querySelector("#course-section");

function coursesUlGenerator(courses) {
    const ulElement = document.createElement("ul");
    ulElement.id = "courses-list";
    for (const course of courses) {
        course.course_type = (course.course_type === 0) ? "자과" : "타과";
        course.alerted = course.alerted === true ? "여석 존재" : "여석 없음";
        const liElement = document.createElement("li");
        ulElement.appendChild(liElement);

        liElement.innerHTML = `
      <div class="course-info">
        <span class="name-span">${course.name}</span>
        <span class="code-span">${course.code}</span>
        <span class="divide-span">|</span>
        <span class="type-span">${course.course_type}</span>
        <span class="divide-span">|</span>
        <span class="alerted-span">${course.alerted}</span>
      </div>
      <div class="delete-course">
        <button  class="delete-course-btn" type="button">삭제</button>
      <div>
    `;
    }
    return ulElement;
}

async function fetchCourse() {
    showSpinner();
    await fetchByGet("/api/applicant/courses",
        (responseData) => {
            if (responseData) {
                appliedCourseStatusElement.children[0].textContent = responseData.length;
                appliedCourseStatusElement.children[1].textContent = '3';
                courseSectionElement.innerHTML = "";
                courseSectionElement.appendChild(coursesUlGenerator(responseData));
                const deleteCourseBtnElement = document.querySelectorAll(".delete-course-btn");
                deleteCourseBtnElement.forEach((button) => button.addEventListener("click", deleteCourse));
            }else{
                courseSectionElement.innerHTML = "<p> 알림 받을 과목이 없습니다</p>";
            }
        },
        (responseData) => {showOkModal(responseData.message);},
    );
    hideSpinner();
}

const addCourse = _.throttle(async () => {
    if (gradeElement.value === "-1"){
        showOkModal("학년을 선택해주세요.", ()=>{});
        return;
    }
    if (typeElement.value === "-1"){
        showOkModal("신청 구분을 선택해주세요.", ()=>{});
        return;
    }
    const bodyData = {
        name: nameElement.value,
        code: codeElement.value.toUpperCase(),
        grade: gradeElement.value,
        course_type: typeElement.value,
    };

    showSpinner();
    await fetchByPost("/api/applicant/course",
        bodyData,
        async () => {
            await fetchCourse();
            showOkModal('신청되었습니다.');},
        (responseData) => {showOkModal(responseData.message)},
    );
    hideSpinner();
}, 1500);

const deleteCourse = _.throttle(async (event) => {
    const selectedCode = event.target.parentElement.parentElement.querySelector(".code-span").textContent;
    const bodyData = { code: selectedCode };

    showSpinner();
    await fetchByDelete("/api/applicant/course",
        bodyData,
        async () => {
            await fetchCourse();
            showOkModal('삭제되었습니다.');},
        (responseData) => {showOkModal(responseData.message)},
    );
    hideSpinner();
}, 500);

fetchCourse();

addCourseBtnElement.addEventListener("click", addCourse);