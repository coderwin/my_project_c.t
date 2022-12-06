package hj.board.ct.web.member.login;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 로그인시 session에 등록되는 회원 정보 form
 */
@Getter @Setter
@ToString
public class LoginMemberFormInSession {

    private Long memberNum;
    private String memberId;
    private String memberName;

    // ** 생성 메서드 ** //

    /**
     * LoginMemberFormInSession 생성
     */
    public static LoginMemberFormInSession createLoginMemberFormInSession(
            Long num,
            String id,
            String name
    ) {
        LoginMemberFormInSession loginMemberFormInSession = new LoginMemberFormInSession();

        loginMemberFormInSession.setMemberNum(num);
        loginMemberFormInSession.setMemberId(id);
        loginMemberFormInSession.setMemberName(name);

        return loginMemberFormInSession;
    }
}
