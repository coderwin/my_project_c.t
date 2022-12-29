package hj.board.ct.web.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 회원 수정에 필요한 form
 */
@Getter @Setter
@ToString
public class MemberUpdateForm {

    private Integer memberNum; // 회원가입 번호/순서 // 나중에 정리하기
//    @NotBlank
    private String memberName; // 회원 이름
//    @NotBlank
    private String memberId; // 회원 아이디
//    @NotBlank
    private String memberPwd; // 회원 비밀번호
    @NotBlank
    private String memberPwdConfirm; // 회원 비밀번호 확인
//    @NotBlank
    private String memberEmail; // 회원 이메일@NotBlank(message = "비어있을 수 없습니다.")
//    @NotBlank
    private String memberPhone1; // 회원 전화번호 1

    private String memberPhone2; // 회원 전화번호 2
//    @NotBlank
    private String memberBirthday; // 회원 생일

    public MemberUpdateForm() {
    }

    public MemberUpdateForm(Integer memberNum, String memberName, String memberId, String memberPwd, String memberEmail, String memberPhone1, String memberPhone2, String memberBirthday) {
        this.memberNum = memberNum;
        this.memberName = memberName;
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.memberEmail = memberEmail;
        this.memberPhone1 = memberPhone1;
        this.memberPhone2 = memberPhone2;
        this.memberBirthday = memberBirthday;
    }

    // == 생성 메서드 == //
    /////// 서비스의 readMoreMember() 에서 사용
    public static MemberUpdateForm createMemberUpdateForm(
            Integer num,
            String id,
            String pwd,
            String name,
            String email,
            LocalDate birthday,
            String phone1,
            String phone2
    ) {

        MemberUpdateForm memberUpdateForm = new MemberUpdateForm();

        memberUpdateForm.setMemberNum(num);
        memberUpdateForm.setMemberId(id);
        memberUpdateForm.setMemberPwd(pwd);
        memberUpdateForm.setMemberName(name);
        memberUpdateForm.setMemberEmail(email);
        memberUpdateForm.setMemberBirthday(memberUpdateForm.changeTypeForBirthdayInMemberUpdateForm(birthday));
        memberUpdateForm.setMemberPhone1(phone1);
        memberUpdateForm.setMemberPhone2(phone2);

        return memberUpdateForm;
    }

    // == 비즈니스 로직 == //
    /**
     * LocalDate을 String으로 바꾸기
     */
    private String changeTypeForBirthdayInMemberUpdateForm(LocalDate birthday) {
        // yyyy-MM-dd 형식을 String으로 바꾸기
        String parseLocalDate = birthday.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return parseLocalDate;
    }

}
