const idElement = document.querySelector("#user_id");
const pwElement = document.querySelector("#password");
const pushTokenElement = document.querySelector("#push_token");
const loginBtnElement = document.querySelector("#spotlight-btn");

const login = _.throttle(async () => {
    // if (pwElement.value === ""){
    //     showOkModal("모바일 기기로 이용해주세요.");
    //     return;
    // }
    const bodyData = {
        'user_id': idElement.value,
        'password': pwElement.value,
        'push_notification_id': pushTokenElement.value
    };

    showSpinner();
    await fetchByPost("/api/user/login",
        bodyData,
        () => {window.location.href = "/course"},
        (responseData) => {showOkModal(responseData.message)},
    );
    hideSpinner();

}, 1500);

loginBtnElement.addEventListener("click", login);