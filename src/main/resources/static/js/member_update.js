
// member update javascript 요청 처리
$(document).ready(function() {

    // 탈퇴시 다시 물어보기
    repeatAnswerAboutWithdrawal();
});

// 탈퇴시 다시 물어보기
let repeatAnswerAboutWithdrawal = function() {

    // 탈퇴버튼 클릭하면
    $('#delete_btn').click(function() {
        // 탈퇴하실지 물어보기
        let askingStr = "정말로 탈퇴하시겠습니까?";
        let answer = confirm(askingStr);
        console.log(`user answer : ${answer}`);
        // true
        if(answer) {
            // controller로 이동
            $.ajax({
                url: `/member/delete`,
                method: 'POST',
                dataType: 'text'
            })
            .done(function(data) {
//                alert("결과 data : " + data);
                let resultMsg = "정상적으로 탈퇴되었습니다."
                alert(resultMsg);
                console.log(resultMsg);
                // 홈으로 이동
                location.href=`/board/list`;
            })
            .fail(function(xhr, status, err) {
                let failedMsg = "탈퇴 요청 처리중 오류 발생! 다시 요청 바랍니다."
                alert(failedMsg);
                console.log("status : " + status);
                // 뒤로 이동하기
                history.go(-1);
            });
            return;
        }
        // false - 돌아가기
        return;
    });
}