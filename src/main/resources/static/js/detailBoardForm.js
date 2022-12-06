
// 게시글 detail javascript 요청 처리
$(document).ready(function() {

    // 삭제 버튼 클릭시 물어보기
    clickDeleteBtn();
});

// 삭제 버튼 클릭시 물어보기
let clickDeleteBtn = function() {
    // 삭제 버튼 클릭시
    $('#deleteSubmitBtn').click(function() {
        // form submit 결정하기
        $('form#delete_btn').on('submit', function(event) {
            // 삭제 버튼 클릭시 물어보기
            let result = confirm("정말로 삭제하시겠습니까?");
            if(result === true) {
                event.stopImmediatePropagation();
                alert("삭제되었습니다.");
                return true;
            } else {
                event.stopImmediatePropagation();
                return false;
            }
        });
    });
}