package hj.board.ct.util;

import org.springframework.validation.BindingResult;

import java.util.regex.Pattern;

/**
 * 패턴 검사를 한다
 */
public class ValidationOfPattern {

    /**
     * 패턴을 검사한 후 맞지 않으면 bindingResult에 에러 저장
     */
    public static void validateJoinValues(String pattern, String value, BindingResult bindingResult, String field, String errorCode, String defaultMessage) {
        // 패턴 검증 결과
        boolean result = Pattern.matches(pattern, value);
        //  pattern과 일치하지 않을 때
        if(!result) {
            bindingResult.rejectValue(field, errorCode, defaultMessage);
        }
    }

}
