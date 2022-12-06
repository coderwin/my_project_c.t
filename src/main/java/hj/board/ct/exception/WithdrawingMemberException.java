package hj.board.ct.exception;

/**
 * 로그인시, 탈퇴회원이면 에러 발생
 */
public class WithdrawingMemberException extends Throwable {

    public WithdrawingMemberException() {
        super();
    }

    public WithdrawingMemberException(String message) {
        super(message);
    }

    public WithdrawingMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public WithdrawingMemberException(Throwable cause) {
        super(cause);
    }

    protected WithdrawingMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
