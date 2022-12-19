
// 댓글 javascript 요청 처리
$(document).ready(function() {

    // 댓글 저장하기
    createReply();

    // 댓글 '수정'버튼 클릭했을 때
    clickUpdateBtn();

    // 댓글 수정하기
    updateReply();

    // 댓글 삭제하기
    deleteReply();

    // 비로그인 사용자가 댓글 등록 클릭
    clickSaveReplyBtnByNonMember();
});



// ** 댓글 저장하기 **//
let createReply = function() {

    // addReplyBtn 클릭하면
    $('#addReplyBtn').on('click', function() {

        // content, rock, boardNum 데이터 가져오기
        let content = $('#replyContent').val();
        let rock = $('#replyRock').val();
        let boardNum = $('#boardNum').val();

        console.log("save reply content : ", content);
        console.log("save reply rock : ", rock);
        console.log("save reply boardNum : ", boardNum);

        // data 생성
        let data = {
            'content' : content,
            'rock' : rock
        }

        // ajax 생성
        $.ajax({
            url: `/board/${boardNum}/reply`,
            data: JSON.stringify(data),
            method: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: 'text'
        })
        .done(function(data) {
            alert("댓글이 작성되었습니다.");
            location.href = `/board/${boardNum}/detail`;
        })
        .fail(function(status, error) {
            alert("댓글 작성 중 오류가 발생했습니다.");
            alert("상태 : " + status);
            alert("error : ", error);
//            location.href = `/board/${boardNum}/detail`;
        })
//        .always(function(xhr, status) {
//            alert("댓글 요청 처리 완료");
//        })
    })
}


//// ** 댓글 수정버튼 클릭시 ** //
//let clickUpdateBtn = function() {
//
//    $('.replyUpdateBtn').click(function() {
//        // 수정하려는 테그의 데이터 가져오기
////        let replyContent = $(this).closest('.replyContentInList').text();
//        let replyContent = $(this).closest('div').siblings('.replyContentInList').text();
//
//        console.log("replycontent : ", replyContent);
//        console.log("replycontent : ", $(this).closest('div'));
//        console.log("replycontent : ", $(this).closest('div').siblings('.replyContentInList'));
//
//        // content의 div를 input으로 바꾸기 + 기존 데이터 넣기
//        $(this).closest('div').siblings('.replyContentInList')
//                .replaceWith("<textarea class=replyUpdateContent placeholder='댓글을 입력하세요.'>" + replyContent + "</textarea>");
//
//        // 비밀번호 hidden을 text로 바꾸기
//        $(this).closest('div').siblings('.replyRock').attr('type', 'text');
//
//        // 수정버튼은 수정완료로 바뀐다
//        $(this).text('수정완료');
//        // 기존 class 삭제하고 class 추가하기
////        $(this).removeClass('replyUpdateBtn');
////        $(this).addClass('replyUpdateBtn2');
//        $(this).attr('class', 'replyUpdateBtn2');
//
//        // '수정완료'면 this의 현재 이벤트 지우기
////        $(this).off('click');
//    });
//}

// ** 댓글 수정버튼 클릭시 v2 ** //
let clickUpdateBtn = function() {

    $('.replyUpdateBtn').click(function() {
        // 수정하려는 테그의 데이터 가져오기
//        let replyContent = $(this).closest('.replyContentInList').text();
        let replyContent = $(this).closest('div').siblings('.replyContentInListBox').find('.replyContentInList').text();

        console.log("replycontent : ", replyContent);
        console.log("replycontent : ", $(this).closest('div'));
        console.log("replycontent : ", $(this).closest('div').siblings('.replyContentInListBox').find('.replyContentInList'));

        // content의 div를 input으로 바꾸기 + 기존 데이터 넣기
        $(this).closest('div').siblings('.replyContentInListBox').find('.replyContentInList')
                .replaceWith("<textarea class=replyUpdateContent placeholder='댓글을 입력하세요.'>" + replyContent + "</textarea>");

        // 비밀번호 hidden을 text로 바꾸기
        $(this).closest('div').siblings('.replyRockBox').find('.replyRock').attr('type', 'text');

        // 수정버튼은 수정완료로 바뀐다
        $(this).after("<button type='button' class='replyUpdateBtn2'>수정완료</button>");
        // 현재 element 지우기
        $(this).remove();

    });
}



// ** 댓글 수정하기 ** //
let updateReply = function() {

    // class replyUpdateBtn2를 클릭하면
    $(document).on('click', '.replyUpdateBtn2', function(e) {

        // reply 번호, content, rock, boardNum 불러오기
        let replyNum = $(this).closest('div').siblings('.replyNum').val();
        let content = $(this).closest('div').siblings('.replyContentInListBox').find('.replyUpdateContent').val();
        let rock = $(this).closest('div').siblings('.replyRockBox').find('.replyRock').val();
        let boardNum = $('#boardNum').val();

        console.log("update replyNum : ", replyNum);
        console.log("update content : ", content);
        console.log("update rock : ", rock);
        console.log("update boardNum : ", boardNum);

        // data 만들기
        let data = {
             'content': content,
             'rock': rock
        }

        // ajax 생성
        $.ajax({
            url: `/board/reply/${replyNum}`,
            data: JSON.stringify(data),
            method: "PATCH",
            contentType: "application/json; charset=utf-8",
            dataType: "text"
        })
        .done(function(data) {
            alert("댓글이 수정되었습니다.");
            location.href=`/board/${boardNum}/detail`;
        })
        .fail(function(xhr, status, err) {
            alert("댓글 수정 중 오류가 발생했습니다.");
            alert("상태 : ", status);
            alert("error : ", err);
        })
//        .always(function(xhr, status) {
//            alert("댓글 수정 요청 처리 완료");
//        });
    });
}


// ** 댓글 삭제하기 ** //
let deleteReply = function() {

    // 삭제 버튼을 클릭하면
    $('.replyDeleteBtn').click(function() {
        // replyNum, boardNum 가져오기
        let replyNum = $(this).closest('div').siblings('.replyNum').val();
        let boardNum = $('#boardNum').val();

        console.log("delete replyNum : ", replyNum);
        // ajax 실행
        $.ajax({
            url: `/board/reply/${replyNum}`,
            method: 'DELETE',
            contentType: 'application/json; charset:utf-8',
            dataType: 'text'
        })
        .done(function(data) {
            alert("댓글이 삭제되었습니다.");
            location.href= `/board/${boardNum}/detail`;
        })
        .fail(function(xhr, status, err) {
            alert("댓글 삭제 중 오류가 발생했습니다.");
            alert("상태 : ", status);
            alert("error : ", err);
        })
//        .always(function(xhr, status) {
//            alert("댓글 삭제 요청 처리 완료");
//        });
    });
}

/** 비로그인 사용자가 댓글 등록 클릭 **/
let clickSaveReplyBtnByNonMember = function() {
    // fake 등록 버튼 클릭시
    $("#fakeAddReplyBtn").click(function() {
        // 로그인 후 이용하세요 띄우기
        console.log("비로그인 사용자 댓글 등록 click!");
        alert("로그인 후 이용하세요");
        return;
    });
}
