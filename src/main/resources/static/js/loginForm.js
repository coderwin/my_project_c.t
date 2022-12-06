
// loginForm javascript 요청 처리
$(document).ready(function() {

    // 회원가입 성공 시, 성공 메시지 띄우기
    succeedJoin();
});

// 회원가입 성공 시, 성공 메시지 띄우기
let succeedJoin = function() {
    // 파라미터 가져오기
    let url = new URL(window.location.href);
    console.log("urlParameters : ", url);
    console.log("type urlParameters : ", typeof(url));

    let params = url.searchParams;
    console.log("url params : ", params);
    console.log("type params : ", typeof(parmas));

    if(params != undefined) {
        // paramter 중 result 값 가져오기
        const result = params.get("result");

        if(result === "succeed") {
            alert("회원가입이 완료되었습니다.");
        }
    }

}