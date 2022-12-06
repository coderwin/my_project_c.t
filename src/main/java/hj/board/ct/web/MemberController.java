package hj.board.ct.web;

import hj.board.ct.service.MemberService;
import hj.board.ct.util.SessionConst;
import hj.board.ct.util.ValidationOfPattern;
import hj.board.ct.web.member.MemberDetailForm;
import hj.board.ct.web.member.MemberForm;
import hj.board.ct.web.member.MemberUpdateForm;
import hj.board.ct.web.member.login.LoginMemberFormInSession;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 회원 정보 확인, 수정 controller
 */
@RequestMapping("/member")
@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 정보 상세 보기 by memberNum
     */
    @GetMapping("/detail")
    @ApiOperation(value = "회원정보 상세보기")
    public String memberDetailForm(@SessionAttribute(
                                           name = SessionConst.LOGIN_MEMBER,
                                           required = false) LoginMemberFormInSession loginMemberFormInSession,
                                   Model model) {
        // memberDetailForm으로 데이터 불러오기
        MemberDetailForm memberDetailForm = memberService.makeMemberDetailForm(loginMemberFormInSession.getMemberNum());

        model.addAttribute("form", memberDetailForm);

        return "members/detailMember";
    }

    /**
     * 회원 정보 수정 하기 form 이동 by memberNum
     */
    @GetMapping("/update")
    @ApiOperation(value = "회원정보 수정 페이지 이동")
    public String memberUpdateForm(
            @SessionAttribute(
            name = SessionConst.LOGIN_MEMBER,
            required = false) LoginMemberFormInSession loginMemberFormInSession,
            Model model) {

        // MemberUpdateForm으로 데이터 불러오기
        MemberUpdateForm memberUpdateForm =
                memberService.readMember(loginMemberFormInSession.getMemberNum());

        model.addAttribute("memberUpdateForm", memberUpdateForm);

        return "members/updateMemberForm";
    }

    /**
     * 회원 정보 수정 하기
     */
    @PostMapping("/update")
    @ApiOperation(value = "회원정보 수정")
    public String updateMember(@Validated @ModelAttribute MemberUpdateForm memberUpdateForm,
                               BindingResult bindingResult,
                               @SessionAttribute(
                                       name = SessionConst.LOGIN_MEMBER,
                                       required = false) LoginMemberFormInSession loginMemberFormInSession) {
        // 데이터 검증
        /// 회원정보 수정 오류
        //  - pattern 검사
        validateJoinValues(memberUpdateForm, bindingResult);

        if(bindingResult.hasErrors()) {
            log.info("/update bindingResult.hasErrors() : {}", bindingResult);
            return "members/updateMemberForm";
        }

        // 검증 성공
        // 회원 번호로 내용 수정하기
        memberService.updateMemberByNum(loginMemberFormInSession.getMemberNum(), memberUpdateForm);

        // 수정 되었는지 확인하기 - 나중에 삭제
        log.info("post /update memberUpdateForm : {}", memberUpdateForm);

        // 회원 상세보기로 바로 이동
        return "redirect:/member/detail";
    }

    // 회원정보 수정 value 입력 오류 검사
    private void validateJoinValues(MemberUpdateForm memberUpdateForm, BindingResult bindingResult) {
        // 아이디 오류
        String patternId = "^[ㄱ-ㅎㅏ-ㅣ가-힣\\w]{1,21}$";
        ValidationOfPattern.validateJoinValues(patternId, memberUpdateForm.getMemberId(), bindingResult, "memberId", "failedWriting", null);

        // 비밀번호 오류
        String patternPwd = "^(?=.*[a-z])(?=.*\\d)(?=.*[?<>~!@#$%^&*_+-])[a-z\\d?<>~!@#$%^&*_+-]{8,21}$";
        ValidationOfPattern.validateJoinValues(patternPwd, memberUpdateForm.getMemberPwd(), bindingResult, "memberPwd", "failedWriting", null);

        // 비밀번호 확인 오류
        String pwd = memberUpdateForm.getMemberPwd();
        String pwdConfirm = memberUpdateForm.getMemberPwdConfirm();
        if(StringUtils.hasText(pwd) && StringUtils.hasText(pwdConfirm)) {
            if(!pwd.equals(pwdConfirm)) {
                bindingResult.rejectValue("memberPwdConfirm", "failedWriting", null);
            }
        }

        // 이름 오류
        String patternName = "^[가-힣|a-zA-Z]+$";
        ValidationOfPattern.validateJoinValues(patternName, memberUpdateForm.getMemberName(), bindingResult, "memberName", "failedWriting", null);

        // 이메일 오류
        String patternEmail = "^\\w+@[a-zA-Z\\d]+\\.[a-zA-Z\\d]+(\\.[a-zA-Z\\d]+)?$";
        ValidationOfPattern.validateJoinValues(patternEmail, memberUpdateForm.getMemberEmail(), bindingResult, "memberEmail", "failedWriting", null);

        // 휴대전화 오류
        String patternPhone1 = "^01(0|[6-9])\\d{4}\\d{4}$";
        ValidationOfPattern.validateJoinValues(patternPhone1, memberUpdateForm.getMemberPhone1(), bindingResult, "memberPhone1", "failedWriting", null);

        // 선택 사항으로 만들기
        // 집전화 오류
//        String patternPhone2 = "^0(2|[3-6][1-5])\\d{3,4}\\d{4}$";
//        ValidationOfPattern.validateJoinValues(patternPhone2, memberUpdateForm.getMemberPhone2(), bindingResult, "memberPhone2", "failedWriting", null);

        // 생일 오류
        String patternBirthday =
                "^" +
                        "(1[0-9][0-9][0-9]|20([0-1][0-9]|2[0-2]))" + // 년도
                        "(" +
                        "(0[1|3|5|7-8]|1[0|2])(0[1-9]|[1-2][0-9]|3[0-1])|" + // 월일
                        "02(0[1-9]|[1-2][0-9])|" +
                        "(0[4|6|9]|11)(0[1-9]|[1-2][0-9]|30)" +
                        ")"+
                        "$";
        ValidationOfPattern.validateJoinValues(patternBirthday, memberUpdateForm.getMemberBirthday(), bindingResult, "memberBirthday", "failedWriting", null);
    }

    /**
     * 회원 탈퇴 하기 by memberNum
     */
    @PostMapping("/delete")
    @ApiOperation(value = "회원 탈퇴")
    public String leaveMember(@SessionAttribute(
            name = SessionConst.LOGIN_MEMBER,
            required = false) LoginMemberFormInSession loginMemberFormInSession, HttpServletRequest request) {

        // 탈퇴 멤버 기록 남기기? 기록 남겨도 되나?
        log.info("member leave /delete : {}", loginMemberFormInSession);

        // 회원 정보 삭제하기
        memberService.deleteMemberV2(loginMemberFormInSession.getMemberNum());

        // session 만료 하기
        HttpSession session = request.getSession(false);

        if(session != null) {
            session.invalidate();
        }

        return "redirect:/board/list";
    }


}
