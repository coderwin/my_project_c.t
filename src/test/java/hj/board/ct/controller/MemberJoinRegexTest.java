package hj.board.ct.controller;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 회원가입시, 정규표현식으로 test
 */
@Slf4j
public class MemberJoinRegexTest {

    // 아이디 확인
    /**
     * 아이디 확인
     */
    @Test
    public void 정규식_아이디() {
        // given
        // 아이디 확인
        // - 한글 영문(대문자, 소문자) ,_,  숫자만 가능 {1,21}
        String pattern = "^[ㄱ-ㅎㅏ-ㅣ가-힣\\w]{1,21}$";

        // when
        String t1 = "가면하하하하하하"; // 한글
        String t2 = "asdf123ㄱㄷ"; // 한글 영어 숫자
        String t3 = "asdf123ㄱㄷㄷㅈㄱㅁㄷㄱ123123";
        String t4 = "asdf123ㄱㄷㄷㅈㄱㅁ ㄷㄱ123123"; // 초과
        String t5 = "asdf123ㄱㄷㄷㅈㄱㅁ ㄷㄱ123#$"; // 기호
        String t6 = "asdf123_Aㅁㄷㄱ1S3ds"; // 혼합
        String t7 = "asdf123_Aㅁㄷㄱ1S3ds"; // 혼합
        String t8 = "ㅏㅑㅡㅠㅣㅜㅐㅔ"; // 모음
        String t9 = "asdf12ㅏ3_Aㅁㄷㄱ1Sㅣ3dㅏs"; // 모음 혼합
        String t10 = " "; // 한칸 띄우기
        String t11 = "asdf"; // 영어
        String t12 = "a"; // 한자만

        // then
        resultTest(pattern, t1, true);
        resultTest(pattern, t2, true);
        resultTest(pattern, t3, true);
        resultTest(pattern, t4, false);
        resultTest(pattern, t5, false);
        resultTest(pattern, t6, true);
        resultTest(pattern, t7, true);
        resultTest(pattern, t8, true);
        resultTest(pattern, t9, true);
        resultTest(pattern, t10, false);
        resultTest(pattern, t11, true);
        resultTest(pattern, t12, true);
    }

    // 결과 테스트
    private void resultTest(String pattern, String t1, boolean expectedValue) {
        boolean result = Pattern.matches(pattern, t1);

        assertThat(result).isEqualTo(expectedValue);
    }


    // 비밀번호 확인
    /**
     * 비밀번호 확인
     */
    @Test
    public void 정규식_비밀번호() {
        // given
        // 아이디 확인
        // - 영문(소문자), 숫자, 특수기호만 가능 {8,21}
        String pattern = "^(?=.*[a-z])(?=.*\\d)(?=.*[?<>~!@#$%^&*_+-])[a-z\\d?<>~!@#$%^&*_+-]{8,21}$";

        // when
        String t1 = "a456@#2d"; // 영어 숫자 특수기호 8
        String t2 = "a456@#2da456@#2da456@"; // 영어 숫자 특수기호 21
        String t3 = "a456@#2da456@#2da456@+"; // 영어 숫자 특수기호 22
        String t4 = "a456@#2da4 56@#2da45"; // 영어 숫자 특수기호 띄어쓰기
        String t5 = "a456@#2dㅇㄹ"; // 한글+
        String t6 = "adsfjejfe"; // 영어만 있을 때
        String t7 = "45679832"; // 숫자만 있을 때
        String t8 = "#$%*_+&^"; // 특수기호만 있을 때
        String t9 = "dfj$#kj+r^"; // 영어 특수기호 만 있을 때
        String t10 = "df23ere2ew"; // 영어 숫자만 있을 때
        String t11 = "@#21%3^34"; // 특수기호 숫자만 있을 때
        String t12 = "@#21%3^34("; // 해당 특수기호 아닌 기호가 있을 때

        // then
        resultTest(pattern, t1, true);
        resultTest(pattern, t2, true);
        resultTest(pattern, t3, false);
        resultTest(pattern, t4, false);
        resultTest(pattern, t5, false);
        resultTest(pattern, t6, false);
        resultTest(pattern, t7, false);
        resultTest(pattern, t8, false);
        resultTest(pattern, t9, false);
        resultTest(pattern, t10, false);
        resultTest(pattern, t11, false);
        resultTest(pattern, t12, false);
    }

    // 이름 확인
    /**
     * 이름 확인
     */
    @Test
    public void 정규식_이름() {
        // given
        // 이름 확인
        // - 한글 영문(대문자, 소문자) 1자 이상
        String pattern = "^[가-힣|a-zA-Z]+$";

        // when
        String t1 = "가나다"; // 한글
        String t2 = "asdfa"; // 영어
        String t3 = "ㅇㄹㄷㅁ;"; // 자음 모음
        String t4 = "12ㅁㄷㅈㄱ1ㄴ5"; // 숫자
        String t5 = "asdf가A나1"; // 혼합

        // then
        resultTest(pattern, t1, true);
        resultTest(pattern, t2, true);
        resultTest(pattern, t3, false);
        resultTest(pattern, t4, false);
        resultTest(pattern, t5, false);
    }

    // 이메일 확인
    /**
     * 이메일 확인
     */
    @Test
    public void 정규식_이메일() {
        // given
        // 아이디 확인
        // - 영문(대문자, 소문자) , _,
        String pattern = "^\\w+@[a-zA-Z\\d]+\\.[a-zA-Z\\d]+(\\.[a-zA-Z\\d]+)?$";

        // when
        String t1 = "abcdABC33@naver.com"; // 영어 숫자
        String t2 = "abcdABC33@naver.com.kr"; // . 하나 더
        String t3 = "1346123@nav.do"; // 아이디 숫자
        String t4 = "1346 123@nav.do"; // 아이디 띄어쓰기
        String t5 = "asd#$12@dfe.dfi"; // 아이디 특수기호

        // then
        resultTest(pattern, t1, true);
        resultTest(pattern, t2, true);
        resultTest(pattern, t3, true);
        resultTest(pattern, t4, false);
        resultTest(pattern, t5, false);
    }

    // 휴대전화 확인
    /**
     * 휴대전화 확인
     */
    @Test
    public void 정규식_휴대전화() {
        // given
        // 휴대전화 확인
        // - 숫자만 11 자리 , _,
        String pattern = "^01(0|[6-9])\\d{4}\\d{4}$";

        // when
        String t1 = "01012341234"; // 010
        String t2 = "01712341234"; // 017
        String t3 = "01512341234"; // 015
        String t4 = "0101231234"; // 10자리
        String t5 = "010 4567895"; // 띄어쓰기
        String t6 = "010-123-123"; // 기호넣기

        // then
        resultTest(pattern, t1, true);
        resultTest(pattern, t2, true);
        resultTest(pattern, t3, false);
        resultTest(pattern, t4, false);
        resultTest(pattern, t5, false);
        resultTest(pattern, t6, false);
    }

    // 집전화 확인
    /**
     * 집전화 확인
     */
    @Test
    public void 정규식_집전화() {
        // given
        // 집전화 확인
        // - 국번 숫자 10,11 자리
        String pattern = "^0(2|[3-6][1-5])\\d{3,4}\\d{4}$";

        // when
        String t1 = "021231234"; // 02 9자리
        String t2 = "0212341234"; // 02 10자리
        String t3 = "0431231234"; // 043 10자리
        String t4 = "05112341234"; // 11자리
        String t5 = "05712341234"; // 057 11자리
        String t6 = "056 4567875"; // 띄어쓰기
        String t7 = "02-123-123"; // 기호넣기
        String t8 = "01812341234"; // 018

        // then
        resultTest(pattern, t1, true);
        resultTest(pattern, t2, true);
        resultTest(pattern, t3, true);
        resultTest(pattern, t4, true);
        resultTest(pattern, t5, false);
        resultTest(pattern, t6, false);
        resultTest(pattern, t7, false);
        resultTest(pattern, t8, false);
    }

    // 생년월일 확인
    /**
     * 생년월일 확인
     *  -- 에러사항 있음 : 년도, 월, 일을 정규식으로만 표현하지 못함 -- update 필요
     */
    @Test
    public void 정규식_생년월일() {
        // given
        // 생년월일 확인
        // - 현재로 1000년 전, 월, 일
        // - 년: 기간을 정하지 못함
        // - 월: OK
        // - 일 : 윤달 구분 못 함
        String pattern =
                "^(1[0-9][0-9][0-9]|20([0-1][0-9]|2[0-2]))" + // 월
                "(" +
                "(0[1|3|5|7-8]|1[0|2])(0[1-9]|[1-2][0-9]|3[0-1])|" + // 일월
                "02(0[1-9]|[1-2][0-9])|" +
                "(0[4|6|9]|11)(0[1-9]|[1-2][0-9]|30)" +
                ")"+
                "$";

        // when
        String t1 = "20000512"; // O
        String t2 = "15621231"; // O
        String t3 = "9011231"; // 900년대
        String t4 = "20001312"; // 13월
        String t5 = "20000532"; // 32일
        String t6 = "2000532"; // 월에 0을 빼먹기
        String t7 = "20120230"; // 2월 30일
        String t8 = "20230521"; // 2023년 05월 21일
        String t9 = "20120631"; // 6월 31일
        String t10 = "21230630"; // 2123년 6월 30일
        String t11 = "2000-5-5"; // - 들어감
        String t12 = "200005052"; // 9자리
        String t13 = "20091131"; // 11월31일
        String t14 = "20091230"; // 12월30일
        String t15 = "2009123"; // 7자리
        String t16 = "20321030"; // 7자리

        // then
        resultTest(pattern, t1, true);
        resultTest(pattern, t2, true);
        resultTest(pattern, t3, false);
        resultTest(pattern, t4, false);
        resultTest(pattern, t5, false);
        resultTest(pattern, t6, false);
        resultTest(pattern, t7, false);
        resultTest(pattern, t8, false);
        resultTest(pattern, t9, false);
        resultTest(pattern, t10, false);
        resultTest(pattern, t11, false);
        resultTest(pattern, t12, false);
        resultTest(pattern, t13, false);
        resultTest(pattern, t14, true);
        resultTest(pattern, t15, false);
        resultTest(pattern, t16, false);
    }















}
