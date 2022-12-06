package hj.board.ct.web.member.login;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 회원 비밀번호 찾기에 필요한 form
 */
@Getter @Setter
@ToString
public class LoginSearchPwdForm {

    private String id; // 회원 아이디
    private String phone; // 회원 전화번호
    private String email; // 회원 이메일

    public LoginSearchPwdForm() {
    }

    // test 위해서
    public LoginSearchPwdForm(String id, String phone, String email) {
        this.id = id;
        this.phone = phone;
        this.email = email;
    }
}
