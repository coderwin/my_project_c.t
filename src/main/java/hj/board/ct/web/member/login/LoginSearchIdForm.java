package hj.board.ct.web.member.login;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원 아이디 찾기에 필요한 form
 */
@Getter @Setter
public class LoginSearchIdForm {

    private String name; // 회원 이름
    private String phone; // 회원 전화번호
    private String email; // 회원 이메일

    public LoginSearchIdForm() {
    }

    // test 위해서
    public LoginSearchIdForm(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
