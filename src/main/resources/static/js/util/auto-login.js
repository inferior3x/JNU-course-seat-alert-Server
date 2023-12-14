window.addEventListener("DOMContentLoaded",
    async () => {
        await fetchByGet(
            "/api/user/authorization",
            (responseData) => {
                if (responseData === true)
                    window.location.href="/course";
                },
            ()=>{}
        );
    }
)