const idElement = document.querySelector("#user_id");
const pwElement = document.querySelector("#password");
const pw2Element = document.querySelector("#password2");
const pushTokenElement = document.querySelector("#push_token");
const signupBtnElement = document.querySelector("#spotlight-btn");

const signup = _.throttle(async () => {
    if (pwElement.value !== pw2Element.value){
        showOkModal("비밀번호가 서로 다릅니다.", ()=>{});
        return;
    }

    const bodyData = {
        'user_id': idElement.value,
        'password': pwElement.value,
    };

    showSpinner();
    await fetchByPost("/api/user/signup",
        bodyData,
        () => { //auto login
            showOkModal("가입되었습니다.",
                async () => {
                    if (pushTokenElement.value === ""){
                        showOkModal("모바일 기기로 이용해주세요.");
                        return;
                    }
                    bodyData['push_notification_id'] = pushTokenElement.value;
                    await fetchByPost("/api/user/login",
                        bodyData,
                        () => {window.location.href = "/course"},
                        (responseData) => {showOkModal(responseData.message)},
                    );
                }
            );
        },
        (responseData) => {showOkModal(responseData.message)},
    );
    hideSpinner();
}, 1500);

signupBtnElement.addEventListener("click", signup);