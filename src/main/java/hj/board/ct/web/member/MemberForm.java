package hj.board.ct.web.member;

import hj.board.ct.domain.MemberChecking;
import hj.board.ct.domain.Phone;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

/**
 * 신규 회원가입에 사용 form
 */
@Getter @Setter
@ToString
public class MemberForm {

//    @NotBlank
    private String memberName; // 회원 이름
//    @NotBlank
    private String memberId; // 회원 아이디
//    @NotBlank
    private String memberPwd; // 회원 비밀번호

    @NotBlank
    private String memberPwdConfirm; // 회원 비밀번호 확인
//    @NotBlank
    private String memberEmail; // 회원 이메일
//    @NotBlank
    private String phone1; // 회원 휴대전화 번호 1
    private String phone2; // 회원 전화번호 2
//    @NotBlank
    private String memberBirthday; // 회원 생일

//    // 회원가입 동의 체크
//
//    private Boolean individualInformationAgreement; // 개인정보 사용 동의
//
//    private Boolean memberJoinAgreement; // 회원가입 동의

    public MemberForm() {
    }

    public MemberForm(String memberName, String memberId, String memberPwd, String memberEmail, String phone1, String phone2, String memberBirthday) {
        this.memberName = memberName;
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.memberEmail = memberEmail;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.memberBirthday = memberBirthday;
    }



}
