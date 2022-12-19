
// member javascript 요청 처리
$(document).ready(function() {

    // 아이디 중복 확인
    confirmDuplicatedId();

    // 비밀번호 보안 알려주기
    announceStrengthSecurityOfPwd();
});

// 회원가입 시, 아이디 중복 확인
let confirmDuplicatedId = function() {

    // 중복확인 클릭시, 아이디 가져오기
    $('#confirmingDuplicatedIdBtn').click(function() {

        // 중복확인 메시지가 있다면 지우기
        $('.duplicatedIdMsg').text("");

        // 아이디 확인
        let memberId = $('#memberId').val();
        console.log("사용 id 확인 : ", memberId);

        // 값이 없으면 값을 입력하라고 말하기
        if(memberId === '') {
            // 메시지 남기기
            let errorMsg = "아이디를 입력하세요.";
            $('#confirmingDuplicatedIdBtn').after("<p class='duplicatedIdMsg duplicatedIdFailMsg'>" + errorMsg + "</p>");
            return;
        }

        $.ajax({
            url: `/join/validation/${memberId}`,
            method: 'GET',
            dataType: 'json'
        })
        .done(function(data) {
            console.log("data : ", data);
//            alert('data : ', data);
            console.log("data.result : ", data.result);

            // 메시지 남기기
            $('#confirmingDuplicatedIdBtn').after("<p class='duplicatedIdMsg duplicatedIdSuccessMsg'>" + data.result + "</p>");
        })
        .fail(function(data, status, err) {
//            console.log("data : ", data);
//            console.log("status : ", status);
//            console.log("err : ", err);
//            alert("data : ", data);
//            alert("data : ", data.code);
//            alert("data : ", data.errorMsg);
//            alert("status : ", status);
//            alert("err : ", err);
//            alert("data type : ", typeof(data));

            // 메시지 남기기
            $('#confirmingDuplicatedIdBtn').after("<p class='duplicatedIdMsg duplicatedIdFailMsg'>" + data.responseJSON.errorMsg + "</p>");
        });
    });
}

// 비밀번호 보안 중 약함, 보통, 강력, 매우 강력 알려주기
let announceStrengthSecurityOfPwd = function() {

    // 키보드 클릭시 이벤트 발생
    $('#memberPwd' ).on('keyup', function() {

        console.log("keyup appears");

        // pwd 가져오기
        let pwd = $('#memberPwd').val();
        // 결과 알려주는 테그 가져오기
        let securityOfPwd = $('#securityOfPwd');
        // 키보드 이벤트 발생할 때마다 reset
        securityOfPwd.text("");
        // securityOfPwd 보이기
        securityOfPwd.show();

        // securityOfPwd 숨기기
        if(pwd === null || pwd === '') {
            securityOfPwd.hide();
        }

        // .*[?<>~!@#$%^&*_+-]가 몇 개인지 확인
        let pattern = /[?<>~!@#$%^&*_+-]/;

        // 개수 마다 약함, 보통, 강력, 매우 강력 알려주기
        // 일치하는 개수 가져오기
//        let matchingPatterns = pattern.exec(pwd);
        let matchingPatterns = pwd.split(pattern);
        console.log("matchingPatterns : ", matchingPatterns);

        // 매칭 개수
        let matchingPatternsThings = matchingPatterns.length;
        console.log(`matchingPatternsThings 개수 : ${matchingPatternsThings}`);

        // 결과 알려주기
        securityOfPwdResult(matchingPatternsThings, securityOfPwd);
    });
}

// 비밀번호 보안 결과 알려주기
let securityOfPwdResult = function(matchingPatternsThings, securityOfPwd) {

    if(matchingPatternsThings === 1) {
        console.log(`announceStrengthSecurityOfPwd 결과 : ${matchingPatternsThings}`);
        securityOfPwd.attr('class', 'securityLight');
        securityOfPwd.text('매우 약함');
    } else if(matchingPatternsThings === 2) {
        console.log(`announceStrengthSecurityOfPwd 결과 : ${matchingPatternsThings}`);
        securityOfPwd.attr('class', 'securityLight');
        securityOfPwd.text('약함');
    } else if(matchingPatternsThings === 3) {
        console.log(`announceStrengthSecurityOfPwd 결과 : ${matchingPatternsThings}`);
        securityOfPwd.attr('class', 'securityNormal');
        securityOfPwd.text('보통');
    } else if(matchingPatternsThings === 4) {
        console.log(`announceStrengthSecurityOfPwd 결과 : ${matchingPatternsThings}`);
        securityOfPwd.attr('class', 'securityStrength');
        securityOfPwd.text('강력');
    } else if(matchingPatternsThings >= 5) {
        console.log(`announceStrengthSecurityOfPwd 결과 : ${matchingPatternsThings}`);
        securityOfPwd.attr('class', 'securityMoreStrength');
        securityOfPwd.text('매우 강력');
    }
}




