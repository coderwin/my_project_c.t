
// 좋아요 javascript 요청 처리
$(document).ready(function() {

    // page upload될 때
    // search-container nameAndEmail div가 보이고
    // search-container nameAndPhone div는 안 보인다.
    $('.search-container.nameAndPhone').hide();
    $('.searchEmailBox').addClass("makingShadow");

    // search box 바꾸기
    clickSearchEmailBox();
    clickSearchPhoneBox();
});

// change search box when clicking EmailBox
let clickSearchEmailBox = function() {
    // searchEmailBox 클릭하면
    $(document).on('click', '.searchEmailBox', function() {
        // search-container nameAndEmail div가 보이고
        $('.search-container.nameAndEmail').show();
        // box 주위에 그림자 생기기
        $('.searchEmailBox').addClass('makingShadow');

        // search-container nameAndPhone div는 안 보인다.
        $('.search-container.nameAndPhone').hide();
        // box 주위에 그림자 지우기
        $('.searchPhoneBox').removeClass('makingShadow');

        // phone input값 초기화 시키기
        $('#phone').val('');
//        $('#phone').val(null);
    });
}

// change search box when clicking PhoneBox
let clickSearchPhoneBox = function() {
    // searchEmailBox 클릭하면
    $(document).on("click", ".searchPhoneBox", function() {
        // search-container nameAndEmail div가 안 보이고
        $('.search-container.nameAndEmail').hide();
        // box 주위에 그림자 지우기
        $('.searchEmailBox').removeClass('makingShadow');

        // search-container nameAndPhone div는 보인다.
        $('.search-container.nameAndPhone').show();
        // box 주위에 그림자 생기기
        $('.searchPhoneBox').addClass('makingShadow');

        // email input값 초기화 시키기
        $('.nameAndEmail #email').val('');
//        $('.nameAndEmail #email').val(null);
    });
}

