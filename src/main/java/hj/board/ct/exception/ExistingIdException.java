package hj.board.ct.exception;

/**
 * 아이디 중복확인 예외
 */
public class ExistingIdException extends RuntimeException {

    public ExistingIdException() {
        super();
    }

    public ExistingIdException(String message) {
        super(message);
    }

    public ExistingIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingIdException(Throwable cause) {
        super(cause);
    }

    protected ExistingIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
