//overlay
const settingOverlayElement = document.querySelector("#setting-overlay");
//setting btn
const settingBtnElement = document.querySelector("#setting-btn");
const closeSettingBtnElement = document.querySelector("#close-setting-btn");
//feature btn of setting
const accountInitBtnElement = document.querySelector('#account-init-btn');
const accountDeleteBtnElement = document.querySelector('#account-delete-btn');
const logoutBtnElement = document.querySelector('#logout-btn');


const deleteAccount = _.throttle(async () => {
        const bodyData = {};

        showSpinner();
        await fetchByDelete("/api/user/withdraw",
            bodyData,
            () => {window.location.href = '/signup'},
            (responseData) => {showOkModal(responseData.message)},
        );
        hideSpinner();
    },
    1500);

const logout = _.throttle(async () => {
        const bodyData = {};

        showSpinner();
        await fetchByPost("/api/user/logout",
            bodyData,
            () => {window.location.href = '/login'},
            (responseData) => {showOkModal(responseData.message)},
        );
        hideSpinner();
    },
    1500);


//setting btn
settingBtnElement.addEventListener('click', () => {
    settingOverlayElement.style.left = '0%';
    //settingOverlayElement.style.display = 'none';
});
closeSettingBtnElement.addEventListener('click', () => {
    settingOverlayElement.style.left = '100%';
    //settingOverlayElement.style.display = 'block';
});

accountDeleteBtnElement.addEventListener('click', () => {
    showQuestionModal('계정이 삭제됩니다.\n진행하시겠습니까?', deleteAccount);
});
logoutBtnElement.addEventListener('click', async () => {
    showQuestionModal('로그아웃 하시겠습니까?', logout);
});
