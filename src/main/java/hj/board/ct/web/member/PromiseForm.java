package hj.board.ct.web.member;

import hj.board.ct.domain.MemberChecking;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 회원 가입시 약속사항 입력
 */
@Getter @Setter
@ToString
public class PromiseForm {

    private Boolean individualInformationAgreement; // 개인정보 이용 동의
    private Boolean memberJoinAgreement; // 회원가입 동의

    public PromiseForm(Boolean individualInformationAgreement, Boolean memberJoinAgreement) {
        this.individualInformationAgreement = individualInformationAgreement;
        this.memberJoinAgreement = memberJoinAgreement;
    }

    // ** 비즈니스 로직 ** //
    // MemberForm의 boolean값을 memberJoinAgreement, individualInformationAgreement 필드값으로 바꾸기
    public MemberChecking changeMemberCheckingValue(Boolean value) {
        if(value == true) {
            return MemberChecking.Y;
        }
        return MemberChecking.N;
    }
}
