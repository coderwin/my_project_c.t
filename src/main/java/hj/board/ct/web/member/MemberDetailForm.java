package hj.board.ct.web.member;

import hj.board.ct.domain.Member;
import hj.board.ct.util.LocalDateParser;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * 회원 상세보기에 필요한 form
 */
@Getter @Setter
@ToString
public class MemberDetailForm {

    private String memberName; // 회원 이름
    private String memberId; // 회원 아이디
    private String memberEmail; // 회원 이메일
    private String memberPhone1; // 회원 전화번호 1
    private String memberPhone2; // 회원 전화번호 2
    private String memberBirthday; // 회원 생일

    // ** 생성 로직 ** //
    public static MemberDetailForm createMemberDetailForm(String id,
                                                          String name,
                                                          String email,
                                                          String phone1,
                                                          String phone2,
                                                          LocalDate birthday) {

        MemberDetailForm memberDetailForm = new MemberDetailForm();

        memberDetailForm.setMemberId(id);
        memberDetailForm.setMemberName(name);
        memberDetailForm.setMemberEmail(email);
        memberDetailForm.setMemberPhone1(phone1);
        memberDetailForm.setMemberPhone2(phone2);
        memberDetailForm.setMemberBirthday(LocalDateParser.changeTypeForBirthdayInMemberUpdateForm(birthday));

        return memberDetailForm;
    }


}
