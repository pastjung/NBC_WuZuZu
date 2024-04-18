import axios from "axios";

//서버 연결하는 부분
export const apiClient = axios.create({
    //baseURL = 서버 주소
    // baseURL: "http://localhost:8080",
    // headers: {
    //     "Access-Control-Allow-Origin": "*",
    //     "Content-Type": "application/json",
    // },
});

//서버에 요청하기 전, 브라우저 localStorage에 저장되어있는 엑세스 토큰을 가져와 header에 넣는다.
apiClient.interceptors.request.use((config) => {
    const accessToken = localStorage.getItem("Authorization");

    if (accessToken && config.headers) {
        config.headers["Authorization"] = `${accessToken}`;
    }
    return config;
});