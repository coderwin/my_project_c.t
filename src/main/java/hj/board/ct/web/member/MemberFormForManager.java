package hj.board.ct.web.member;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.MemberChecking;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 관리자가 회원정보를 확인하는데 필요한 form
 */
@Getter @Setter
public class MemberFormForManager {

    private Integer memberNum; // 회원 번호
    private String memberName; // 회원 이름
    private String memberId; // 회원 아이디
    private String memberEmail; // 회원 이메일
    private String phone1; // 회원 전화번호 1
    private String phone2; // 회원 전화번호 2
    private String memberBirthday; // 회원 생일
    private MemberChecking memberIndividualInformationAgreement; // 개인정보 사용 동의
    private MemberChecking memberJoinAgreement; // 회원가입 동의
    private List<Board> boardList; // 회원이 쓴 게시글 가져오기

    // == 생성 메서드 == //
    public static MemberFormForManager createMemberFormForManager(
            Integer num,
            String name,
            String id,
            String email,
            String phone1,
            String phone2,
            String birthday,
            MemberChecking individualInformationAgreement,
            MemberChecking memberJoinAgreement
    ) {
        MemberFormForManager memberFormForManager = new MemberFormForManager();

        memberFormForManager.setMemberNum(num);
        memberFormForManager.setMemberName(name);
        memberFormForManager.setMemberId(id);
        memberFormForManager.setMemberEmail(email);
        memberFormForManager.setPhone1(phone1);
        memberFormForManager.setPhone2(phone2);
        memberFormForManager.setMemberBirthday(birthday);
        memberFormForManager.setMemberIndividualInformationAgreement(individualInformationAgreement);
        memberFormForManager.setMemberJoinAgreement(memberJoinAgreement);

        return memberFormForManager;
    }
}
