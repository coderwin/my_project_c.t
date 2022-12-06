
// 비밀번호 button 클릭 javascript 요청 처리
$(document).ready(function() {
    // 비밀번호 button의 input이 text일 때, password로 바꾸기
    changeTypeAboutPwd();
    // 비밀번호 button 클릭시 보이시, 가리기
    showAndScreenPwd();
});

// 비밀번호 button의 input이 text일 때, password로 바꾸기
let changeTypeAboutPwd = function() {
    $('.showPwd').siblings("input[type='text']").attr('type', 'password');
}

// 비밀번호 button 클릭시 보일시, 가리기
let showAndScreenPwd = function() {

    let changeType = 0; // label type 바꿔주는 변수
//    alert("changeType value : " + changeType);

    // 비밀번호 버튼 클릭
    $('.showPwd').click(function() {
        if(changeType === 0) {
            // type password를 text로 바꾸기
            $(this).siblings("input[type='password']").attr('type', 'text');
//            alert("change text");
            changeType = 1;
        } else {
            // type text를 password로 바꾸기
            $(this).siblings("input[type='text']").attr('type', 'password');
//            alert("change password");
            changeType = 0;
        }
    });







}