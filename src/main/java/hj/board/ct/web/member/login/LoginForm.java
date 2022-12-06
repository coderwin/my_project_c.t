package hj.board.ct.web.member.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 로그인에 사용 form
 */
@Getter @Setter
@ToString
public class LoginForm {

    private String loginId; // 로그인 아이디
    private String loginPwd; // 로그인 비밀번호

    private Boolean rememberId; // 아이디 기억
}
