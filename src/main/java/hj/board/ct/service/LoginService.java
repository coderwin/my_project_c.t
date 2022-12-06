package hj.board.ct.service;

import hj.board.ct.domain.Member;
import hj.board.ct.domain.SubscriptionStatusOfMember;
import hj.board.ct.exception.WithdrawingMemberException;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.web.member.login.LoginMemberFormInSession;
import hj.board.ct.web.member.login.LoginSearchIdForm;
import hj.board.ct.web.member.login.LoginSearchPwdForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * 고객이 로그인 시도 시, 회원인지 확인
     */
    public Member confirmLogin(String id, String pwd) throws WithdrawingMemberException {
        // 고객이 로그인 시도시, 회원인지 확인
        Member member = memberRepository.findMemberByLoginId(id)
                .filter((m) -> m.getMemberPwd().equals(pwd))
                .orElse(null);

        // 탈퇴회원인지 조사하기
        if(member != null) {
            // 탈퇴회원 조사
            if(member.getSubscriptionStatus().equals(SubscriptionStatusOfMember.W)) {
                // 탈퇴회원이면 예외 발생
                String errorMsg = "탈퇴한 회원입니다.";
                throw new WithdrawingMemberException(errorMsg);
            }
        }

        return member;
    }

    /**
     * 회원 아이디 찾기
     */
    public List<String> searchId(LoginSearchIdForm loginSearchIdForm) {

        // db로 가서 찾기
        if(StringUtils.hasText(loginSearchIdForm.getPhone())) {
            return memberRepository.findIdByNameAndPhone(loginSearchIdForm.getName(), loginSearchIdForm.getPhone());
        } else {
            return memberRepository.findIdByNameAndEmail(loginSearchIdForm.getName(), loginSearchIdForm.getEmail());
        }
    }

    /**
     * 회원 비밀번호 찾기
     */
    public List<String> searchPwd(LoginSearchPwdForm loginSearchPwdForm) {

        // db로 가서 찾기
        if(!StringUtils.hasText(loginSearchPwdForm.getPhone())) {
            return memberRepository.findPwdByIdAndEmail(loginSearchPwdForm.getId() , loginSearchPwdForm.getEmail());
        } else {
            return memberRepository.findPwdByIdAndPhone(loginSearchPwdForm.getId(), loginSearchPwdForm.getPhone());
        }
    }





}
