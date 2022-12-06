package hj.board.ct.Service;

import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import hj.board.ct.exception.WithdrawingMemberException;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.service.LoginService;
import hj.board.ct.service.MemberService;
import hj.board.ct.web.member.login.LoginMemberFormInSession;
import hj.board.ct.web.member.login.LoginSearchIdForm;
import hj.board.ct.web.member.login.LoginSearchPwdForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class LoginServiceTest {

    @Autowired
    LoginService loginService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;


    /**
     * 회원 아이디 찾기 확인
     */
    @Test
    public void 회원_아이디_찾기() {
        // given
        // 회원 정보 저장
        Member member = Member.createMember("a1",
                "123%%^",
                "hello",
                "a23@a12",
                "20101030",
                MemberChecking.Y,
                MemberChecking.Y,
                "010-1234",
                "051-123");

        memberRepository.save(member);
        // when // then
        /////// 이름, 휴대폰번호로
        findIdTest("hello", "010-1234", "", member.getMemberId());
        /////// 이름 다른 경우
        findIdTest("hello23", "010-1234", "");
        ////// 이름. 이메일로
        findIdTest("hello", "", "a23@a12", member.getMemberId());
        /////// 이메일 다른 경우
        findIdTest("hello", "", "a23@a125c");

    }

    // 회원 아이디 찾기 확인 test에 사용
    private void findIdTest(String name, String phone, String email, String... ids) {
        // 아이디 찾기 진행
        /////// 이름, 휴대폰번호로
        LoginSearchIdForm loginSearchIdForm = new LoginSearchIdForm(name, phone, email);
        List<String> findIdList = loginService.searchId(loginSearchIdForm);

        // then
        // 회원 정보와 일치하는 지 확인
        assertThat(findIdList).containsExactly(ids);
    }

    /**
     * 회원 비밀번호 찾기
     */
    @Test
    public void 회원_비밀번호_찾기() {
        // given
        // 회원 정보 저장
        Member member = Member.createMember("a1",
                "123%%^",
                "hello",
                "a23@a12",
                "20101030",
                MemberChecking.Y,
                MemberChecking.Y,
                "010-1234",
                "051-123");

        memberRepository.save(member);
        // when // then
        /////// id, 휴대폰번호로
        findPwdTest("a1", "010-1234", "", member.getMemberPwd());
        /////// id 다른 경우
        findPwdTest("a123", "010-1234", "");
        ////// id. 이메일로
        findPwdTest("a1", "", "a23@a12", member.getMemberPwd());
        /////// 이메일 다른 경우
        findPwdTest("a1", "", "a23@a125c");

    }

    // 회원 아이디 찾기 확인 test에 사용
    private void findPwdTest(String id, String phone, String email, String... ids) {
        // 아이디 찾기 진행
        /////// 이름, 휴대폰번호로
        LoginSearchPwdForm loginSearchPwdForm = new LoginSearchPwdForm(id, phone, email);
        List<String> findIdList = loginService.searchPwd(loginSearchPwdForm);

        // then
        // 회원 정보와 일치하는 지 확인
        assertThat(findIdList).containsExactly(ids);
    }


    // 수정 전 사용 test
//    /**
//     * 로그인 가능 회원인지 확인 + LoginMemberFormInSession 객체 만들어졌는지 확인
//     */
//    @Test
//    public void 로그인_가능_회원인지_확인() {
//        // given
//        // 회원 등록
//        Member member = Member.createMember("a1",
//                "123%%^",
//                "hello",
//                "a23@a12",
//                "20101030",
//                MemberChecking.Y,
//                MemberChecking.Y,
//                "010-1234",
//                "051-123");
//
//        memberRepository.save(member);
//
//        // 회원가입시 생성 될, LoginMemberFormInSession 객체 만들기
//        LoginMemberFormInSession loginMemberFormInSession =
//                LoginMemberFormInSession.createLoginMemberFormInSession(
//                        member.getMemberNum(),
//                        member.getMemberId(),
//                        member.getMemberName()
//                );
//
//        // when // then
//        // 아이디, 비밀번호 일치 할 때
//        loginTest(member.getMemberId(), member.getMemberPwd(), loginMemberFormInSession);
//        // 아이디 틀렸을 때
//        loginTest("a12", member.getMemberPwd(), null);
//        // 비밀번호 틀렸을 때
//        loginTest(member.getMemberId(), "123%%^12", null);
//        // 아이디, 비밀번호 틀렸을 때
//        loginTest("a12", "123%%^12", null);
//    }
//
//    // 로그인_가능_회원인지_확인 사용
//    private void loginTest(String id, String pwd, LoginMemberFormInSession testLoginMemberFormInSession) {
//        // when
//        // 회원인지 확인
//        Member loginMember
//                = loginService.confirmLogin(id, pwd);
//
//        LoginMemberFormInSession findSessionForm
//                = loginService.confirmLogin(id, pwd);
//
//        if(testLoginMemberFormInSession != null) {
//            // then
//            // 객체 데이터 끼리 비교
//            assertThat(findSessionForm.getMemberNum()).isEqualTo(testLoginMemberFormInSession.getMemberNum());
//            assertThat(findSessionForm.getMemberId()).isEqualTo(testLoginMemberFormInSession.getMemberId());
//            assertThat(findSessionForm.getMemberName()).isEqualTo(testLoginMemberFormInSession.getMemberName());
//        } else {
//            // then
//            // 회원이 아닐 때
//            org.junit.jupiter.api.Assertions.assertSame(findSessionForm, testLoginMemberFormInSession);
//        }
//    }

    /**
     * 로그인 가능 회원인지 확인
     */
    @Test
    public void 로그인_가능_회원인지_확인V2() throws WithdrawingMemberException {
        // given
        // 회원 등록
        Member member = Member.createMember("a1",
                "123%%^",
                "hello",
                "a23@a12",
                "20101030",
                MemberChecking.Y,
                MemberChecking.Y,
                "010-1234",
                "051-123");

        memberRepository.save(member);

        // 회원가입시 생성 될, LoginMemberFormInSession 객체 만들기

        // when
        loginMemberTest("a1", "123%%^", member);
        // 아이디 틀렸을 때
        loginMemberTest("a12", member.getMemberPwd(), null);
        // 비밀번호 틀렸을 때
        loginMemberTest(member.getMemberId(), "123%%^12", null);
        // 아이디, 비밀번호 틀렸을 때
        loginMemberTest("a12", "123%%^12", null);
    }

    /**
     * 로그인 가능 회원인지 확인 - 탈퇴회원인 경우
     */
    @Test
    public void 로그인_가능_회원인지_확인V3() {
        // given
        // 회원 등록
        Member member = Member.createMember("a1",
                "123%%^",
                "hello",
                "a23@a12",
                "20101030",
                MemberChecking.Y,
                MemberChecking.Y,
                "010-1234",
                "051-123");

        memberRepository.save(member);

        // 회원가입시 생성 될, LoginMemberFormInSession 객체 만들기

        // when
        // 회원 탈퇴
        memberService.deleteMemberV2(member.getMemberNum());

        // then
        Assertions.assertThrows(Throwable.class, () -> loginMemberTest("a1", "123%%^", member));
        Assertions.assertThrows(WithdrawingMemberException.class, () -> loginMemberTest("a1", "123%%^", member));
//        Assertions.assertThrows(IllegalStateException.class, () -> loginMemberTest("a1", "123%%^", member));
    }

    private void loginMemberTest(String id, String pwd, Member member) throws WithdrawingMemberException {
        // 로그인 시도
        Member loginMember = null;

        loginMember = loginService.confirmLogin(id, pwd);

        // then
        // 아이디, 비밀번호 맞았을 때
        assertThat(loginMember).isEqualTo(member);
    }


}
