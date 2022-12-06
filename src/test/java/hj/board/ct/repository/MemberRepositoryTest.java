package hj.board.ct.repository;

import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import hj.board.ct.domain.Phone;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    /**
     * 회원 정보 저장 확인
     */
    @Test
//    @Rollback(false)
    public void 회원정보_저장() {
        // given
        Member member = Member.createMember("test1",
                "12345",
                "하하",
                "test@test",
                "19991212",
                MemberChecking.Y,
                MemberChecking.N,
                "051-1234",
                "010-1234"
                );

        // when
        memberRepository.save(member); // 영속성 컨텍스트에 위치하고 있다

        // then
        Member findMember = memberRepository.findByNum(member.getMemberNum());

        assertThat(findMember).isInstanceOf(Member.class);
    }

    /**
     * 모든 회원 정보 불러오기 확인
     */
    @Test
//    @Rollback(value = false)
    @DisplayName("모든 Member 가져오기")
    public void 모든_Member데이터() {
        // given

        Member member1 = Member.createMember("test1123",
                "123",
                "하하",
                "test@test",
                "19990202",
                MemberChecking.Y,
                MemberChecking.N,
                "010-1234",
                "051-1234");

        memberRepository.save(member1);

        Member member2 = Member.createMember("test1231",
                "12345",
                "하하2",
                "test2@test",
                "20001212",
                MemberChecking.Y,
                MemberChecking.Y,
                "010-5623",
                "052-365");

        memberRepository.save(member2);

        // when, then
        // 조건 있을 때, 없을 때 확인하기
        findAllTest("", null, null, member1, member2);
        findAllTest("2", null, null, member2);
        findAllTest("", "19990202", null, member1);
        findAllTest("", null, "20001212");// members가 없다
        findAllTest("2", "20001212", null, member2);
        findAllTest("","19990202", "20221021", member1);
    }

    /**
     * 회원 정보 조회 by id로 확인
     */
    @Test
    @DisplayName("회원 정보 조회 by id")
    public void 회원_정보_조회_byId() {
        // given
        // given
        Member member = Member.createMember("test314",
                "12345",
                "하하",
                "test@test",
                "19991212",
                MemberChecking.Y,
                MemberChecking.N,
                "051-1234",
                "010-1234"
        );

        memberRepository.save(member);

        // when
        Member findMember = memberRepository
                .findById(member.getMemberId())
                .orElse(null);

        Member findMember2 = memberRepository
                .findById("hello")
                .orElse(null);

        // then
        assertThat(findMember).isEqualTo(member);
        assertThat(findMember2).isEqualTo(null);
    }

    /**
     * 회원 정보 삭제 확인
     */
    @Test
    @DisplayName("회원 정보 삭제")
    public void 회원_정보_삭제하기() {
        // given
        Member member = Member.createMember("test312",
                "12345",
                "하하",
                "test@test",
                "19991212",
                MemberChecking.Y,
                MemberChecking.N,
                "051-1234",
                "010-1234");

        memberRepository.save(member);

        // when
        // 회원 정보 삭제하기
        memberRepository.delete(member);

        // then
        // 회원 정보 조회
        assertThrows(NoSuchElementException.class,
                () -> memberRepository.findById(member.getMemberId()).orElseThrow());

    }

    // 모든_Member데이터 when then 부분 만들기
    public void findAllTest(String id, String Birthday, String joindate, Member... members) {
        MemberSearchCond memberCondition = new MemberSearchCond(id, changeTypeForLocaldate(Birthday), joindate);

        List<Member> result = memberRepository.findAll(memberCondition);

//        assertThat(result).containsExactly(members);
        assertThat(result).contains(members);
    }

    // String을 localDate로 바꾸기
    private LocalDate changeTypeForLocaldate(String memberBirthday) {
        if(memberBirthday == null) {
            return null;
        }
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss.SSS");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(memberBirthday, formatter);
        return date;
    }

    // String을 localDateTime로 바꾸기
    private LocalDateTime changeTypeForLocaldateTime(String memberJoindate) {
        if(memberJoindate == null) {
            return null;
        }
//       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss.SSS");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime date = LocalDateTime.parse(memberJoindate, formatter);
        return date;
    }

    /*************  회원 아이디, 비밀번호 찾기  **************/

    /**
     * 회원 아이디 찾기 확인 by name and email
     */
    @Test
    public void 회원_아이디_찾기_ByNameAndEmail() {
        // given
        // member 저장
        Member member = Member.createMember("test312",
                "12345",
                "하하3",
                "test@test",
                "19991212",
                MemberChecking.Y,
                MemberChecking.N,
                "051-1234",
                "010-1234");

        memberRepository.save(member);

        // when
        // 아이디 찾기
        List<String> findIdList = memberRepository.findIdByNameAndEmail("하하3", "test@test");
        List<String> emptyFindList = memberRepository.findIdByNameAndEmail("test312", "test@test");

        // then
        // 회원이 있는지 확인하기
        assertThat(findIdList).containsExactly(member.getMemberId());

        // 회원 정보가 다를 때 확인
        assertThat(emptyFindList.size()).isEqualTo(0);

    }

    /**
     * 회원 아이디 찾기 확인 by name and phone
     */
    @Test
    public void 회원_아이디_찾기_ByNameAndPhone() {
        // given
        // member 저장
        Member member = Member.createMember("test312",
                "12345",
                "하하3",
                "test@test",
                "19991212",
                MemberChecking.Y,
                MemberChecking.N,
                "010-1234",
                "051-1234"
                );

        memberRepository.save(member);

        // when
        // 아이디 찾기
        List<String> findIdList = memberRepository.findIdByNameAndPhone("하하3", "010-1234");
        List<String> emptyFindList = memberRepository.findIdByNameAndPhone("test312", "051-1234");

        // then
        // 회원이 있는지 확인하기
        assertThat(findIdList).containsExactly(member.getMemberId());

        // 회원 정보가 다를 때 확인
        assertThat(emptyFindList.size()).isEqualTo(0);

    }

    /**
     * 회원 아이디 찾기 확인 by id and phone
     */
    @Test
    public void 회원_비밀번호_찾기_ByIdAndPhone() {
        // given
        // member 저장
        Member member = Member.createMember("test312",
                "12345",
                "하하3",
                "test@test",
                "19991212",
                MemberChecking.Y,
                MemberChecking.N,
                "010-1234",
                "051-1234"
                );

        memberRepository.save(member);

        // when
        // 아이디 찾기
        List<String> findPwdList = memberRepository.findPwdByIdAndPhone("test312", "010-1234");
        List<String> emptyFindList = memberRepository.findPwdByIdAndPhone("test312", "051-1234");

        // then
        // 회원이 있는지 확인하기
        assertThat(findPwdList).containsExactly(member.getMemberPwd());

        // 회원 정보가 다를 때 확인
        assertThat(emptyFindList.size()).isEqualTo(0);

    }

    /**
     * 회원 비밀번호 찾기 확인 by id and email
     */
    @Test
    public void 회원_비밀번호_찾기_ByIdAndEmail() {
        // given
        // member 저장
        Member member = Member.createMember("test312",
                "12345",
                "하하3",
                "test@test",
                "19991212",
                MemberChecking.Y,
                MemberChecking.N,
                "051-1234",
                "010-1234");

        memberRepository.save(member);

        // when
        // 아이디 찾기
        List<String> findIdList = memberRepository.findPwdByIdAndEmail("test312", "test@test");
        List<String> emptyFindList = memberRepository.findIdByNameAndEmail("test312", "test3@test");

        // then
        // 회원이 있는지 확인하기
        assertThat(findIdList).containsExactly(member.getMemberPwd());

        // 회원 정보가 다를 때 확인
        assertThat(emptyFindList.size()).isEqualTo(0);

    }

    /*************로그인시 아이디 session 저장에 필요 ***********/
    /**
     * 로그인 시, 회원 찾기 확인
     */
    @Test
    public void 로그인_시_회원_있는지_찾기() {
        // given
        // 회원 저장
        Member member = Member.createMember("test312",
                "12345",
                "하하3",
                "test@test",
                "19991212",
                MemberChecking.Y,
                MemberChecking.N,
                "010-1234",
                "051-1234");

        memberRepository.save(member);

        // when
        // 회원 찾기
        Member loginMember = memberRepository.findMemberByLoginId(member.getMemberId()).get();

        // then
        // 회원 존재하는지 확인
        assertThat(loginMember).isEqualTo(member);

        // 존재하지 않는 회원 아이디 일 때
        Assertions.assertThrows(NoSuchElementException.class,
                () -> {
            memberRepository.findMemberByLoginId("hellogk").orElseThrow();
        });
    }

















}
