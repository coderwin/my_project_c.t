
// 게시글 update javascript 요청 처리
$(document).ready(function() {

    // delete uploadFiles
    deleteUploadFiles();
});

// delete uploadFiles
let deleteUploadFiles = function() {

    // uploadFileDeleteBtn click
    $('.image-group .uploadFileDeleteBtn').click(function() {
        // 정말로 삭제하시겠습니까 물어보기
        let answer = confirm("정말로 삭제하시겠습니까?");
//        alert("answer of uploadefile delete : " + answer);

        if(answer === true) {
            console.log("이미지 삭제 처리 시작");
            // uploadFile 번호 가져오기
            let uploadFileNum = $(this).siblings('.uploadFileNum').val();
//            alert("uploadFileNum : " + uploadFileNum);

            // ajax로 uploadFile delete 하기
            $.ajax({
                url: `/board/uploadFile/${uploadFileNum}`,
                method: 'DELETE'
            })
            .done(function(data) {
                // 완료 하면 삭제되었다 말하기
                alert("정상적으로 처리 되었습니다.");
            })
            .fail(function(data, status, err) {
                alert("삭제 중 에러 발생!!!");
            })
            .always(function() {
                console.log("이미지 삭제 처리 끝");
                // 새로 고침하기
                location.reload();
            });
        }
    });
}