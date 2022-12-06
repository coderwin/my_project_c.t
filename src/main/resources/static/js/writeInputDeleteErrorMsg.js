
// input에 글을 적으면 error글 지우기 javascript 요청 처리
$(document).ready(function() {
    // 에러 메시지가 있으면 input 작성시 해당 input errorMsg 지우기
    deleteErrorMsg();
});

// 에러 메시지가 있으면 input 작성시 해당 input errorMsg 지우기
let deleteErrorMsg = function() {
    // errorMsg가 있으면
    let filedError = $('p.filedError');

    if(filedError != null) {
        // input에 keydown 일어날 때
        $('input').on('keydown', function() {
            // 해당 input errorMsg 지운다
            $(this).siblings('p.fieldError').hide();
            // color black으로 바꾸기
            $(this).css('color', '#000000');
        });
    }
}