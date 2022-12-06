package hj.board.ct.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * API 에러를 객체(json)으로 던져준다
 */
@Getter @Setter
public class ErrorResultMsg {

    private String code; // 에러 코드
    private String errorMsg; // 에러 메시지

    // ** 생성 로직 ** //
    public static ErrorResultMsg createErrorResultMsg(String code, String msg) {

        ErrorResultMsg errorResultMsg = new ErrorResultMsg();

        errorResultMsg.setCode(code);
        errorResultMsg.setErrorMsg(msg);

        return errorResultMsg;
    }

}
