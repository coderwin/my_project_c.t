
// 게시글 list javascript 요청 처리
$(document).ready(function() {

    // 그림, 제목 클릭시 상세보기 입장
    isBoardPwd();

    // 날짜 형식에 맞춰서 검색
    searchByWritingdate();

    // 검색 조건이 모두 비워있을
    clickSearchBtnWithEmptyValues();
});

// 그림, 제목 클릭시 비밀번호 있다면
let isBoardPwd = function() {

    // board 데이터 가져오기
    $(document).on('click', '.moreDetailBtn', function() {
        // 게시글 번호 가져오기
//        let boardNum = $(this).closest('p').siblings('.boardNum').val();
        let boardNum = $(this).closest('.boardLi').find('.boardNum').val();
//        console.log("text : " + $(this).closest('p').text());
        console.log("boardNum : " + boardNum);

        // 게시글 비밀번호 담는 변수
        let boardPwd = "";

        // 게시글 비밀번호 가져오기
        $.ajax({
            url: `/board/pwd/${boardNum}`,
            method: "GET",
            dataType: "json"
        })
        .done(function(data) {
            console.log(`data : ${data}`);
//            alert("pwd : " + data.boardPwd);
            // 비밀번호 받기
            boardPwd = data.boardPwd;
            // 게시글 이동 여부 확인
            moveLocation(boardNum, boardPwd);
        })
        .fail(function(data, status, err) {
            console.log("에러 발생 " + status);
            alert("에러 발생 " + status);
        });
    });
}

// 게시글 비밃번호 value에 따른 장소 이동
//   - isBoardPwd 사용
let moveLocation = function(boardNum, boardPwd) {

    if(boardPwd === null || boardPwd === '') {
        // 바로 이동하기
        location.href=`/board/${boardNum}/detail`;
        return;
    // boardPwd 있으면
    } else {
        // 알려주기
        alert("이 게시글은 비밀글로 설정되어 있습니다.");
        // 비밀번호 확인하기
        let result = prompt("비밀번호를 입력하세요.");
//        alert("confirm result : " + result);
        console.log("confirm result : " + result);

        // 취소 클릭
        if(result === null) {
            // 아무 응답 없음
            return;
        }
        // 맞으면 입장
        else if(result === boardPwd) {
            location.href=`/board/${boardNum}/detail`;
            return;
        }
        else if(result != boardPwd) {
            // 틀리면 그대로 (return)
            alert("비밀번호가 틀렸습니다.");
            return;
        }

    }
}

// 날짜 형식에 맞게 검색 from form
let searchByWritingdate = function() {
    // 검색 클릭 시

    $('#searchBtn').click(function() {
        // submit 될 시
        $('form').on('submit', function(event) {
            // 날짜 0 or 8이 아니면 return
            let writingdate = $('#writingdate').val();
            let writingdateLength = writingdate.length;
            console.log("search writingdate : ", writingdate);

            // 생일 오류
            const patternWritingdate =
                            /^(1[0-9][0-9][0-9]|20([0-1][0-9]|2[0-2]))((0[1|3|5|7-8]|1[0|2])(0[1-9]|[1-2][0-9]|3[0-1])|02(0[1-9]|[1-2][0-9])|(0[4|6|9]|11)(0[1-9]|[1-2][0-9]|30))$/;

            let patternResult = patternWritingdate.test(writingdate);
            console.log("patternResult : ", patternResult);

//            if(writingdate === 0 || writingdate === 8) {
//                return true;
//            } else {
//                alert("날짜 형식에 맞추어 입력해주세요");
//                event.stopImmediatePropagation();
//                return false;
//            }

            if(writingdateLength === 0 || patternResult) {
                event.stopImmediatePropagation();
                return true;
            } else {
                alert("날짜 형식에 맞추어 입력해주세요");
                event.stopImmediatePropagation();
                return false;
            }
        });
    });
}

// 검색 조건이 모두 비워있을
// - 때 검색 조건이 비어있다 말하기
let clickSearchBtnWithEmptyValues = function() {
    // 검색 클릭 시
    $('#searchBtn').click(function() {
        // submit 될 시
        $('form').on('submit', function(event) {
            // id, title, content, writingdate value 가져오기
            let id = $('#id').val();
            let title = $('#title').val();
            let content = $('#content').val();
            let writingdate = $('#writingdate').val();

            console.log("search id : ", id);
            console.log("search title : ", title);
            console.log("search content : ", content);
            console.log("search writingdate : ", writingdate);

            if(id === "" && title === "" && content === "" && writingdate === "") {
                alert("검색 조건을 입력해주세요.");
                event.stopImmediatePropagation();
                return false;
            }
            else if(id === null && title === null && content === null && writingdate === null) {
                alert("검색 조건을 입력해주세요.");
                event.stopImmediatePropagation();
                return false;
            }
        });
    });
}

