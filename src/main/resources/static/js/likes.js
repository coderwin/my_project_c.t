
// 좋아요 javascript 요청 처리
$(document).ready(function() {
    // 로딩시 실행
    showLikesImage();

    // 로그인 안 했을 때, 좋아요 클릭
    nonLogin();

    // /board/list 요청 시, 좋아요를 출력해준다
    changeLikesImage();
});

// login 안 한 경우
const nonLogin = function() {
    $('.fakeHeartImage').click(function() {
        console.log('로그인 안 하고 click');
        let pathName = $(location).attr('pathname');

        console.log('pathName : ', pathName);
        alert("로그인 후 이용하세요");
        location.href=`/login?resultURL=${pathName}`;
    });
}

// login 한 경우

//$(document).ready(function() {
//    // .likeBox 클릭시
//    $('.likeBox').on('click', function() {
//        console.log("likeBox click!!!!!");
//        // image가 'heart.png' -> 'fullheart.png'로 바뀐다
//        $(this).find('.heartImage').attr('src', "/images/fullheart.png");
//    });
//
//    // .likeBox 클릭시
//});

// /board/list 요청 시, 좋아요를 출력해준다 -- 가리기
let showLikesImage = function() {

    // boardNum 가져오기
    let boardLiList = $('.boardLi');
    console.log("boardNumList : ", boardLiList );

    boardLiList.each(function() {

        console.log("this : ", $(this));
        // 각각의 li의 heartImage 불러오기
        let heartImage = $(this).find('.likeBox').find('.heartImage');

        // boardNum 불러오기
        let boardNum = $(this).find('.boardNum').val();

//        let heartImage = boardNum.nextSibling.nextSibling.nextSibling.find('.heartImage');
        console.log("boardNum : " + boardNum + " heartImage : " + heartImage);

        if(boardNum === "" || boardNum === null ) {
            // 처리 끝내기
            return;
        }

        // ajax 통신
        $.ajax({
            url: "/board/likes",
            data: {
                'boardNum': boardNum
            },
            method: "GET",
            dataType: "text"
        })
        .done(function(data) {
            if(data == 0) {
                heartImage.attr('src', '/images/heart.png');
            } else {
                heartImage.attr('src', '/images/fullheart.png');
            }
        })
        .fail(function(xhr, status, err) {
            console.log("showLikesImage(), 이미지 가져오기 실패");
        })
    });

}

// 좋아요 클릭했을 때, likes 이미지 바꾸기
let changeLikesImage = function() {

    $('.likeBox').click(function() {
        // boardNum 가져오기
        let boardNum = $(this).closest("p").siblings('.boardNum').val();
        console.log('click boardNum : ', boardNum);

        // 좋아요 value 가져오기
        let likesSizeText = $(this).find('.likesSize').text();
        let likesSizeValue = parseInt(likesSizeText); // 문자를 숫자로 바꾸기
        console.log('click likesSizeText: ', likesSizeText);

        console.log("$(this).find('.heartImage').attr('src') : ", $(this).find(".heartImage").attr('src'));

        let heartImage = $(this).find(".heartImage"); // image 불러오기
        let likesSize = $(this).find('.likesSize'); // span likesSize 불러오기

        // heart.png일 때
        if($(this).find(".heartImage").attr('src') === '/images/heart.png') {
            // db에 likes 추가하기
            $.ajax({
                url: `/board/likes`,
                data: {
                    'boardNum': boardNum
                },
                method: "POST"
            })
            .done(function(data) {
                // 이미지 '/images/fullheart.png'로 바꾸기
                console.log("$(this).find('.heartImage').attr('src') : ", heartImage.attr('src'));
                heartImage.attr('src', '/images/fullheart.png');
                // 좋아요 수 1 증가시키기
                likesSize.text(likesSizeValue + 1);
            })
            .fail(function(xhr, status, err) {
                alert("좋아요 요청 작업에 오류가 발생했습니다.");
                alert(err.message);
            });

        // fullheart.png일 때
        } else if($(this).find(".heartImage").attr('src') === '/images/fullheart.png') {
            // db에 likes 삭제하
            $.ajax({
                url: `/board/likes`,
                data: {
                    'boardNum': boardNum
                },
                method: "DELETE"
            })
            .done(function(data) {
                // 이미지 '/images/fullheart.png'로 바꾸기
                heartImage.attr('src', '/images/heart.png');
                // 좋아요 수 1 증가시키기
                likesSize.text(likesSizeValue - 1);
            })
            .fail(function(xhr, status, err) {
                alert("좋아요 요청 작업에 오류가 발생했습니다.");
                alert(err.message);
            });
        }
    });


}