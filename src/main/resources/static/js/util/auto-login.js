window.addEventListener("DOMContentLoaded",
    async () => {
        await fetchByGet(
            "/api/user/authentication",
            (responseData) => {
                if (responseData === true)
                    window.location.href="/course";
                },
            ()=>{}
        );
    }
)