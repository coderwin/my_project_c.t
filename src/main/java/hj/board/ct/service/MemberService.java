package hj.board.ct.service;

import hj.board.ct.domain.Member;
import hj.board.ct.domain.Phone;
import hj.board.ct.domain.SubscriptionStatusOfMember;
import hj.board.ct.exception.ExistingIdException;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.repository.MemberSearchCond;
import hj.board.ct.web.member.MemberDetailForm;
import hj.board.ct.web.member.MemberFormForManager;
import hj.board.ct.web.member.MemberUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원정보 저장하기
     */
    @Transactional
    public void join(Member member) {
        // 아이디 중복 확인
        validateDuplicatedId(member);

        // 회원 저장
        memberRepository.save(member);
    }

    /**
     * 아이디 중복 확인
     *  - join(), RestfulApi에 사용
     */
    private void validateDuplicatedId(Member member) {
        // 회원 정보에서 아이디가 같은게 있는지 검사
        if(!memberRepository.findById(member.getMemberId()).isEmpty()) {
            throw new ExistingIdException("사용중인 아이디 입니다");
        }
    }

    /**
     * 회원정보 수정하기 by member_num
     */
    @Transactional
    public void updateMemberByNum(Long memberNum, MemberUpdateForm memberUpdateForm) {
        Member updateMember = memberRepository.findByNum(memberNum);

        // 수정할 값 입력하기
        Phone updatePhone = new Phone(memberUpdateForm.getMemberPhone1(), memberUpdateForm.getMemberPhone2());

        updateMember.setMemberName(memberUpdateForm.getMemberName());
        updateMember.setMemberPwd(memberUpdateForm.getMemberPwd());
        updateMember.setMemberEmail(memberUpdateForm.getMemberEmail());
        updateMember.setMemberPhone(updatePhone);
        updateMember.setMemberBirthday(updateMember.changeTypeForBirthday(memberUpdateForm.getMemberBirthday()));
        updateMember.setMemberUpdatedate(LocalDateTime.now());
    }

    /**
     * 회원정보 수정하기 by member_id
     */
    @Transactional
    public void updateMemberById(String memberId, MemberUpdateForm memberUpdateForm) {
        Member updateMember = memberRepository.findById(memberId).get();

        // 수정할 값 입력하기
        Phone updatePhone = new Phone(memberUpdateForm.getMemberPhone1(), memberUpdateForm.getMemberPhone2());

        updateMember.setMemberName(memberUpdateForm.getMemberName());
        updateMember.setMemberPwd(memberUpdateForm.getMemberPwd());
        updateMember.setMemberEmail(memberUpdateForm.getMemberEmail());
        updateMember.setMemberPhone(updatePhone);
        updateMember.setMemberBirthday(updateMember.changeTypeForBirthday(memberUpdateForm.getMemberBirthday()));
        updateMember.setMemberUpdatedate(LocalDateTime.now());
    }

    /**
     * 회원정보 삭제하기 by member_num : 완전 삭제
     */
    @Transactional
    public void deleteMemberV1(Long memberNum) {
        // 회원 정보 가져오기
        Member deleteMember = memberRepository.findByNum(memberNum);
        // 삭제하기
        memberRepository.delete(deleteMember);
    }

    /**
     * 회원정보 삭제하기 by member_num : 사용자가 사용 못 하게 db에만 보관
     */
    @Transactional
    public void deleteMemberV2(Long memberNum) {
        // 회원 정보 가져오기
        Member deleteMember = memberRepository.findByNum(memberNum);
        // 탈퇴날짜 입력하기
        deleteMember.setMemberWithdrawaldate(LocalDateTime.now());
        // 회원의 가입상태 탈퇴로 만들기
        deleteMember.setSubscriptionStatus(SubscriptionStatusOfMember.W);
    }

    /**
     * 회원정보 삭제하기 by member_id
     */
    @Transactional
    public void deleteMember(String memberId) {
        // 회원 정보 가져오기
        Member member = memberRepository.findById(memberId).get();
        // 삭제하기
        memberRepository.delete(member);
    }

    /**
     * 필요한 회원정보 가져오기 for detail of member by memberNum
     */
    public MemberUpdateForm readMember(Long memberNum) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByNum(memberNum);

        // MemberUpdateForm으로 데이터 옮기기
        MemberUpdateForm memberUpdateForm = MemberUpdateForm.createMemberUpdateForm(
                member.getMemberNum(),
                member.getMemberId(),
                member.getMemberPwd(),
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberBirthday(),
                member.getMemberPhone().getPhone1(),
                member.getMemberPhone().getPhone2());

        return memberUpdateForm;
    }

    /**
     * 필요한 회원정보 가져오기 for detail of member by memberId
     */
    public MemberUpdateForm readMember(String memberId) {
        // 회원 정보 가져오기
        Member member = memberRepository.findById(memberId).get();

        // MemberUpdateForm으로 데이터 옮기기
        MemberUpdateForm memberUpdateForm = MemberUpdateForm.createMemberUpdateForm(
                member.getMemberNum(),
                member.getMemberId(),
                member.getMemberPwd(),
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberBirthday(),
                member.getMemberPhone().getPhone1(),
                member.getMemberPhone().getPhone2());

        return memberUpdateForm;
    }



    /**
     * 모든 회원정보 가져오기
     */
    public List<MemberFormForManager> readMoreMembers(MemberSearchCond memberSearchCond) {
        List<MemberFormForManager> memberFormForManagers = new ArrayList<>();

        memberRepository.findAll(memberSearchCond).forEach(m -> {
           MemberFormForManager memberFormForManager = MemberFormForManager.createMemberFormForManager(
                    m.getMemberNum(),
                    m.getMemberName(),
                    m.getMemberId(),
                    m.getMemberEmail(),
                    m.getMemberPhone().getPhone1(),
                    m.getMemberPhone().getPhone2(),
                    m.getMemberBirthday().toString(),
                    m.getIndividualInformationAgreement(),
                    m.getMemberJoinAgreement()
            );

           memberFormForManagers.add(memberFormForManager);
        });

        return memberFormForManagers;
    }

    //************  view, controller 만드는 중 추가 메서드 ********************//
    /**
     * 필요한 회원정보 가져오기 for detail of member by memberNum
     */
    public MemberDetailForm makeMemberDetailForm(Long memberNum) {
        // 회원 정보 가져오기
        Member member = memberRepository.findByNum(memberNum);

        // MemberDetailForm으로 데이터 옮기기
        MemberDetailForm memberDetailForm = MemberDetailForm.createMemberDetailForm(
                member.getMemberId(),
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberPhone().getPhone1(),
                member.getMemberPhone().getPhone2(),
                member.getMemberBirthday()
        );

        return memberDetailForm;
    }

    /**
     * 필요한 회원정보 가져오기 for detail of member by memberId
     */
    public MemberDetailForm makeMemberDetailForm(String memberId) {
        // 회원 정보 가져오기
        Member member = memberRepository.findById(memberId).get();

        // MemberDetailForm으로 데이터 옮기기
        MemberDetailForm memberDetailForm = MemberDetailForm.createMemberDetailForm(
                member.getMemberId(),
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberPhone().getPhone1(),
                member.getMemberPhone().getPhone2(),
                member.getMemberBirthday()
        );

        return memberDetailForm;
    }

    /**
     * 아이디가 존재하는지 확인
     */
    public void confirmingDuplicatedId(String id) {

        log.info("memberService confirmingDuplicatedId");

        // null이 아니면 사용 불가능
        // - ExistingIdException 던지기
        if(!memberRepository.findById(id).isEmpty()) {
            String errorMsg = "사용중인 아이디 입니다.";
            throw new ExistingIdException(errorMsg);
        }
    }
}
