package hj.board.ct.web;

import hj.board.ct.exception.ErrorResultMsg;
import hj.board.ct.exception.ExistingIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * API exception을 처리해준다
 */
@RestControllerAdvice
@Slf4j
public class ApiExceptionController {

    /**
     * 회원가입 시, 아이디 중복이 발생하는 예외
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistingIdException.class)
    public @ResponseBody ErrorResultMsg existingIdExHandle(ExistingIdException e) {
        log.error("exception appears : ", e);

        // 에러 메시지 전달하기
        ErrorResultMsg errorResultMsg = ErrorResultMsg.createErrorResultMsg("bad request", e.getMessage());

        return errorResultMsg;
    }


}
