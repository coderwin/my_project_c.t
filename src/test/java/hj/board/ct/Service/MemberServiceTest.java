package hj.board.ct.Service;

import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import hj.board.ct.domain.SubscriptionStatusOfMember;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.repository.MemberSearchCond;
import hj.board.ct.service.MemberService;
import hj.board.ct.web.member.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    /**
     * 회원 정보 저장 확인
     */
    @Test
//    @Rollback(value = false)
    public void 회원정보_저장() {
        // given
        MemberForm memberForm = new MemberForm("t1",
                "t1",
                "123!@",
                "hello@he",
                "010-2565",
                "052-236",
                "20011111"
        );

        // 약속사항 입력
        PromiseForm promiseForm = new PromiseForm(true, false);

        Member member = Member.createMember(memberForm.getMemberId(),
                memberForm.getMemberPwd(),
                memberForm.getMemberName(),
                memberForm.getMemberEmail(),
                memberForm.getMemberBirthday(),
                promiseForm.changeMemberCheckingValue(promiseForm.getIndividualInformationAgreement()),
                promiseForm.changeMemberCheckingValue(promiseForm.getMemberJoinAgreement()),
                memberForm.getPhone1(),
                memberForm.getPhone2());

        memberService.join(member);

        // when
        // memberRepository에서 회원 저장되었는지 확인하기
        Member findMember = memberRepository.findByNum(member.getMemberNum());

        // then
        assertThat(findMember).isEqualTo(findMember);
    }

    /**
     * 회원 아이디 중복 확인
     */
    @Test
    public void 회원_아이디_중복() {
        // given
        MemberForm memberForm = new MemberForm("t1",
                "t1",
                "123!@",
                "hello@he",
                "010-2565",
                "052-236",
                "20011111");
        // 약속사항 입력
        PromiseForm promiseForm = new PromiseForm(true, false);

        Member member = Member.createMember(memberForm.getMemberId(),
                memberForm.getMemberPwd(),
                memberForm.getMemberName(),
                memberForm.getMemberEmail(),
                memberForm.getMemberBirthday(),
                promiseForm.changeMemberCheckingValue(promiseForm.getIndividualInformationAgreement()),
                promiseForm.changeMemberCheckingValue(promiseForm.getMemberJoinAgreement()),
                memberForm.getPhone1(),
                memberForm.getPhone2());

        memberService.join(member);

        // given
        MemberForm memberForm2 = new MemberForm("t1",
                "t1",
                "123!@",
                "hello@he",
                "010-2565",
                "052-236",
                "20011111");

        Member member2 = Member.createMember(memberForm.getMemberId(),
                memberForm.getMemberPwd(),
                memberForm.getMemberName(),
                memberForm.getMemberEmail(),
                memberForm.getMemberBirthday(),
                promiseForm.changeMemberCheckingValue(promiseForm.getIndividualInformationAgreement()),
                promiseForm.changeMemberCheckingValue(promiseForm.getMemberJoinAgreement()),
                memberForm.getPhone1(),
                memberForm.getPhone2());

        // when // then
        Throwable exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
        // 예외 메시지 확인하기
//        org.junit.jupiter.api.Assertions.assertEquals("사용중인 아이디 입니다2", exception.getMessage());
        org.junit.jupiter.api.Assertions.assertEquals("사용중인 아이디 입니다", exception.getMessage());
    }

    /**
     * 회원 정보 수정 확인 by num
     */
    @Test
//    @Rollback(value = false)
    public void 회원_정보_수정_byNum() {
        // given
        // 회원정보 저장
        Member member = Member.createMember("t1","123!@",
                "t1",
                "hello@he",
                "20011111",
                MemberChecking.Y,
                MemberChecking.N,
                "010-2565",
                "052-236");

        memberRepository.save(member);

        // when
        // 수정하기
        MemberUpdateForm memberUpdateForm = new MemberUpdateForm(member.getMemberNum(),
                "t2",
                member.getMemberId(),
                "t2",
                "hello@he",
                "010-2565",
                "052-236",
                "20011111");

        memberService.updateMemberByNum(memberUpdateForm.getMemberNum(), memberUpdateForm);

        // then
        // 다시 아이디 가져와서 회원정보 확인
        Member updateMember = memberRepository.findByNum(member.getMemberNum());

        assertThat(updateMember).isEqualTo(member);
        assertThat(updateMember.getMemberName()).isEqualTo("t2");
//        assertThat(updateMember.getMemberName()).isEqualTo("t1");

    }

    /**
     * 회원 정보 수정 확인 by id
     *  - 궁금한점 : update할 때, 왜 where절에 member_num이 있을까? member_id가 아니고
     */
    @Test
//    @Rollback(value = false)
    public void 회원_정보_수정_byId() {
        // given
        // 회원정보 저장
        Member member = Member.createMember("t1","123!@",
                "t1",
                "hello@he",
                "20011111",
                MemberChecking.Y,
                MemberChecking.N,
                "010-2565",
                "052-236");

        memberRepository.save(member);

        // when
        // 수정하기
        MemberUpdateForm memberUpdateForm = new MemberUpdateForm(member.getMemberNum(),
                "t2",
                member.getMemberId(),
                "t2",
                "hello@he",
                "010-2565",
                "052-236",
                "20011111");

        memberService.updateMemberById(memberUpdateForm.getMemberId(), memberUpdateForm);

        // then
        // 다시 아이디 가져와서 회원정보 확인
        Member updateMember = memberRepository.findById(member.getMemberId()).get();

        assertThat(updateMember).isEqualTo(member);
        assertThat(updateMember.getMemberName()).isEqualTo("t2");
//        assertThat(updateMember.getMemberName()).isEqualTo("t1");

    }

    /**
     * 회원 정보 완전 삭제 확인 by member_num
     */
    @Test
    public void 회원_정보_삭제ByNum() {
        // given
        // 회원정보 저장
        Member member = Member.createMember("t1","123!@",
                "t1",
                "hello@he",
                "20011111",
                MemberChecking.Y,
                MemberChecking.N,
                "010-2565",
                "052-236");

        memberRepository.save(member);

        // when
        Member deleteMember = memberRepository.findByNum(member.getMemberNum());

        memberService.deleteMemberV1(deleteMember.getMemberNum());

        // then
        log.info("member.getMemberNum() = {}", member.getMemberNum());
        log.info("member.getMemberNum() = {}", deleteMember.getMemberNum());

        assertThat(memberRepository.findByNum(deleteMember.getMemberNum())).isEqualTo(null);
//        assertThat(memberRepository.findByNum(deleteMember.getMemberNum())).isEqualTo(member);
    }

    /**
     * 회원_가입_상태_변경 확인
     */
    @Test
//    @Rollback(value = false)
    public void 회원_가입상태_변경() {
        // given
        // 회원정보 저장
        Member member = Member.createMember("t1","123!@",
                "t1",
                "hello@he",
                "20011111",
                MemberChecking.Y,
                MemberChecking.N,
                "010-2565",
                "052-236");

        memberRepository.save(member);

        // when
        Member deleteMember = memberRepository.findByNum(member.getMemberNum());

        memberService.deleteMemberV2(deleteMember.getMemberNum());

        // then
        log.info("member.getMemberNum() = {}", member.getMemberNum());
        log.info("member.getMemberNum() = {}", deleteMember.getMemberNum());

        Member findMember = memberRepository.findByNum(deleteMember.getMemberNum());

        // 탈퇴 상태 확인
        assertThat(findMember.getSubscriptionStatus()).isEqualTo(SubscriptionStatusOfMember.W);
        assertThat(findMember.getSubscriptionStatus().getSubscriptionStatus()).isEqualTo("withdrawal");
    }

    /**
     * 회원 정보 삭제 확인 by member_id
     */
    @Test
    public void 회원_정보_삭제ById() {
        // given
        // 회원정보 저장
        Member member = Member.createMember("t1", "123!@",
                "t1",
                "hello@he",
                "20011111",
                MemberChecking.Y,
                MemberChecking.N,
                "010-2565",
                "052-236");

        memberRepository.save(member);

        // when
        Member deleteMember = memberRepository.findById(member.getMemberId()).get();

        memberService.deleteMember(deleteMember.getMemberId());

        // then
        log.info("member.getMemberNum() = {}", member.getMemberId());
        log.info("member.getMemberNum() = {}", deleteMember.getMemberId());

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class,
                () -> {memberRepository.findById(deleteMember.getMemberId()).orElseThrow();}
        );
    }

    /**
     * 필요한 회원 정보 불러오기 _ 상세보기 _ by Num
     */
    @Test
    public void 필요한_회원정보_불러오기ByNum() {
        // given
        // 회원정보 저장
        Member member = Member.createMember("t1", "123!@",
                "t1",
                "hello@he",
                "20011111",
                MemberChecking.Y,
                MemberChecking.N,
                "010-2565",
                "052-236");

        memberRepository.save(member);

        // when
        MemberUpdateForm memberUpdateForm = memberService.readMember(member.getMemberNum());

        // then
        assertThat(memberUpdateForm.getMemberNum()).isEqualTo(member.getMemberNum());
        assertThat(memberUpdateForm.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(memberUpdateForm.getMemberEmail()).isEqualTo(member.getMemberEmail());
        assertThat(memberUpdateForm.getMemberPwd()).isEqualTo(member.getMemberPwd());
        assertThat(memberUpdateForm.getMemberBirthday()).isEqualTo(member.getMemberBirthday().toString());
        assertThat(memberUpdateForm.getMemberPhone1()).isEqualTo(member.getMemberPhone().getPhone1());
        assertThat(memberUpdateForm.getMemberPhone2()).isEqualTo(member.getMemberPhone().getPhone2());
    }

    /**
     * 필요한 회원 정보 불러오기 _ 상세보기 _ by Id
     */
    @Test
    public void 필요한_회원정보_불러오기ById() {
        // given
        // 회원정보 저장
        Member member = Member.createMember("t1", "123!@",
                "t1",
                "hello@he",
                "20011111",
                MemberChecking.Y,
                MemberChecking.N,
                "010-2565",
                "052-236");

        memberRepository.save(member);

        // when
        MemberUpdateForm memberUpdateForm = memberService.readMember(member.getMemberId());

        // then
        assertThat(memberUpdateForm.getMemberNum()).isEqualTo(member.getMemberNum());
        assertThat(memberUpdateForm.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(memberUpdateForm.getMemberEmail()).isEqualTo(member.getMemberEmail());
        assertThat(memberUpdateForm.getMemberPwd()).isEqualTo(member.getMemberPwd());
        assertThat(memberUpdateForm.getMemberBirthday()).isEqualTo(member.getMemberBirthday().toString());
        assertThat(memberUpdateForm.getMemberPhone1()).isEqualTo(member.getMemberPhone().getPhone1());
        assertThat(memberUpdateForm.getMemberPhone2()).isEqualTo(member.getMemberPhone().getPhone2());
    }

    /**
     * 모든 회원 정보 가져오기 + 조건에 맞춰서 => 비밀번호 빼고
     */
    @Test
    public void 모든_회원_정보_불러오기() {
        // given
        // 회원정보 저장
        Member member = Member.createMember("t1", "123!@",
                "t1",
                "hello@he",
                "20011111",
                MemberChecking.Y,
                MemberChecking.N,
                "010-2565",
                "052-236");

        memberRepository.save(member);

        // when // then
        // 1) 아무 조건 없을 때
        findAllTest("", "", "", 3);
        // 2) id만 있을 때
        findAllTest("t12", "", "", 1);
        // 3) birthday만 있을 때
        findAllTest("", "19990202", "", 1);
        // 3) joindate만 있을 때
        findAllTest("", "", "20221019", 3);
        // 4) id, birthday만 있을 때
        findAllTest("t", "19980101", "", 0);
        // 5) id, joindate만 있을 때
        findAllTest("test1", "", "20221019", 2);
        // 6) birthday, joindate만 있을 때
        findAllTest("", "19990202", "20221019", 1);
        // 7) id, birthday, joindate 있을 때
        findAllTest("t1", "20011111", "20221018", 0);



    }

    // 모든_회원_정보_불러오기() 에서 사용되는 확인 메서드
    private void findAllTest(String id, String birthday, String joindate, int resultNum) {
        // 조건 정하기
        MemberSearchCond memberSearchCond = new MemberSearchCond(
                id,
                changeTypeForBirthday(birthday),
                joindate);

        List<MemberFormForManager> result = memberService.readMoreMebers(memberSearchCond);

        // then
        assertThat(result.size()).isEqualTo(resultNum);
    }

    // String을 LocalDate로 바꾸기
    private LocalDate changeTypeForBirthday(String memberBirthday) {
        if(StringUtils.hasText(memberBirthday)) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss.SSS");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate date = LocalDate.parse(memberBirthday, formatter);
            return date;
        }
        return null;
    }

    //************  view, controller 만드는 중 추가 메서드 ********************//

    /**
     * 필요한 회원정보 가져오기 for detail of member by memberNum 확인
     */
    @Test
    public void DetailForm에_회원_정보_담기_by_num() {
        // given
        // 회원정보 저장
        Member member = Member.createMember("t1", "123!@",
                "t1",
                "hello@he",
                "20011111",
                MemberChecking.Y,
                MemberChecking.N,
                "010-2565",
                "052-236");

        memberRepository.save(member);

        // when
        // DetailForm에 담기
        MemberDetailForm memberDetailForm = memberService.makeMemberDetailForm(member.getMemberNum());

        // then
        assertThat(memberDetailForm.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(memberDetailForm.getMemberName()).isEqualTo(member.getMemberName());
        assertThat(memberDetailForm.getMemberEmail()).isEqualTo(member.getMemberEmail());
        assertThat(memberDetailForm.getMemberBirthday()).isEqualTo(member.getMemberBirthday().toString());
        assertThat(memberDetailForm.getMemberPhone1()).isEqualTo(member.getMemberPhone().getPhone1());
        assertThat(memberDetailForm.getMemberPhone2()).isEqualTo(member.getMemberPhone().getPhone2());
    }

    /**
     * 필요한 회원정보 가져오기 for detail of member by memberId 확인
     */
    @Test
    public void DetailForm에_회원_정보_담기_by_id() {
        // given
        // 회원정보 저장
        Member member = Member.createMember("t1", "123!@",
                "t1",
                "hello@he",
                "20011111",
                MemberChecking.Y,
                MemberChecking.N,
                "010-2565",
                "052-236");

        memberRepository.save(member);

        // when
        // DetailForm에 담기
        MemberDetailForm memberDetailForm = memberService.makeMemberDetailForm(member.getMemberId());

        // then
        assertThat(memberDetailForm.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(memberDetailForm.getMemberName()).isEqualTo(member.getMemberName());
        assertThat(memberDetailForm.getMemberEmail()).isEqualTo(member.getMemberEmail());
        assertThat(memberDetailForm.getMemberBirthday()).isEqualTo(member.getMemberBirthday().toString());
        assertThat(memberDetailForm.getMemberPhone1()).isEqualTo(member.getMemberPhone().getPhone1());
        assertThat(memberDetailForm.getMemberPhone2()).isEqualTo(member.getMemberPhone().getPhone2());
    }

















}
