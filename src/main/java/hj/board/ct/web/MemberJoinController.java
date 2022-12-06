package hj.board.ct.web;

import hj.board.ct.domain.Member;
import hj.board.ct.exception.ExistingIdException;
import hj.board.ct.service.MemberService;
import hj.board.ct.util.ValidationOfPattern;
import hj.board.ct.web.member.MemberForm;
import hj.board.ct.web.member.PromiseForm;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberJoinController {

    private final MemberService memberService;

    /**
     * 회원 가입 시, 약속사항 체크하는 곳으로 이동하기
     */
    @GetMapping("/join")
    @ApiOperation(value = "회원가입 페이지 이동")
    public String joinPromiseForm(@ModelAttribute PromiseForm promiseForm) {
        return "members/createPromiseForm";
    }

    /**
     * 약속사항 체크 후, 회원상세정보로 넘어가기
     */
    @PostMapping("/join")
    @ApiOperation(value = "회원가입 약속사항 등록")
    public String createPromise(@ModelAttribute PromiseForm promiseForm, Model model) {

        log.info("promiseForm : {}", promiseForm);
        log.info("promiseForm : {}", promiseForm.getIndividualInformationAgreement());
        log.info("promiseForm : {}", promiseForm.getMemberJoinAgreement());

        // 약속사항 체크 넘겨주기
        model.addAttribute("promiseForm", promiseForm);

        // memberForm 넘겨주기
        MemberForm memberForm = new MemberForm();
        model.addAttribute("memberForm", memberForm);

        return "members/createMemberForm";
    }

    /**
     * 회원가입 하기
     */
    @PostMapping("/join/detail")
    @ApiOperation(value = "회원 등록")
    public String createMember(@ModelAttribute PromiseForm promiseForm,
                               @Validated @ModelAttribute("memberForm") MemberForm memberForm,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes
                               ) {

        log.info("memberForm : {}", memberForm.toString());
        log.info("promiseForm : {}", promiseForm.toString());

        /// 회원가입 오류
        //  - pattern 검사
        validateJoinValues(memberForm, bindingResult);

        // 오류 내보내기
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "members/createMemberForm";
        }

        // Member 만들기
        Member member = Member.createMember(
                memberForm.getMemberId(),
                memberForm.getMemberPwd(),
                memberForm.getMemberName(),
                memberForm.getMemberEmail(),
                memberForm.getMemberBirthday(),
                promiseForm.changeMemberCheckingValue(promiseForm.getIndividualInformationAgreement()),
                promiseForm.changeMemberCheckingValue(promiseForm.getMemberJoinAgreement()),
                memberForm.getPhone1(),
                memberForm.getPhone2()
                );

        // 회원가입하기
        try {
            memberService.join(member);

            // 아이디 중복 예외 발생
        } catch(ExistingIdException e) {
            log.error("error appear : ", e);

            // 에러 뷰에 전달하기
            bindingResult.rejectValue("memberId", "failedId", new Object[] {memberForm.getMemberId()}, null);

            if(bindingResult.hasErrors()) {
                log.info("bindingReuslt : {}", bindingResult);
                return "members/createMemberForm";
            }
        }

        // 회원가입 성공
        // 회원가입 성공 메시지 보내주기
        redirectAttributes.addAttribute("result", "succeed");
        return "redirect:/login";
    }

    // 회원가입 value 입력 오류 검사
    private void validateJoinValues(MemberForm memberForm, BindingResult bindingResult) {
        // 아이디 오류
        String patternId = "^[ㄱ-ㅎㅏ-ㅣ가-힣\\w]{1,21}$";
        ValidationOfPattern.validateJoinValues(patternId, memberForm.getMemberId(), bindingResult, "memberId", "failedWriting", null);

        // 비밀번호 오류
        String patternPwd = "^(?=.*[a-z])(?=.*\\d)(?=.*[?<>~!@#$%^&*_+-])[a-z\\d?<>~!@#$%^&*_+-]{8,21}$";
        ValidationOfPattern.validateJoinValues(patternPwd, memberForm.getMemberPwd(), bindingResult, "memberPwd", "failedWriting", null);

        // 비밀번호 확인 오류
        String pwd = memberForm.getMemberPwd();
        String pwdConfirm = memberForm.getMemberPwdConfirm();
        if(StringUtils.hasText(pwd) && StringUtils.hasText(pwdConfirm)) {
            if(!pwd.equals(pwdConfirm)) {
                bindingResult.rejectValue("memberPwdConfirm", "failedWriting", null);
            }
        }

        // 이름 오류
        String patternName = "^[가-힣|a-zA-Z]+$";
        ValidationOfPattern.validateJoinValues(patternName, memberForm.getMemberName(), bindingResult, "memberName", "failedWriting", null);

        // 이메일 오류
        String patternEmail = "^\\w+@[a-zA-Z\\d]+\\.[a-zA-Z\\d]+(\\.[a-zA-Z\\d]+)?$";
        ValidationOfPattern.validateJoinValues(patternEmail, memberForm.getMemberEmail(), bindingResult, "memberEmail", "failedWriting", null);

        // 휴대전화 오류
        String patternPhone1 = "^01(0|[6-9])\\d{4}\\d{4}$";
        ValidationOfPattern.validateJoinValues(patternPhone1, memberForm.getPhone1(), bindingResult, "phone1", "failedWriting", null);

        // 선택 사항으로 만들기
        // 집전화 오류
//        String patternPhone2 = "^0(2|[3-6][1-5])\\d{3,4}\\d{4}$";
//        ValidationOfPattern.validateJoinValues(patternPhone2, memberForm.getPhone2(), bindingResult, "phone2", "failedWriting", null);

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
        ValidationOfPattern.validateJoinValues(patternBirthday, memberForm.getMemberBirthday(), bindingResult, "memberBirthday", "failedWriting", null);
    }

    //************** RestAPI **************//
    /**
     * 아이디 중복 확인
     */
    @PostMapping("/join/validation/{id}")
    @ApiOperation(value = "아이디 중복 확인")
    public @ResponseBody Map<String, String> confirmingDuplicatedId(@PathVariable String id) {

        log.info("validateDuplicatedId Id : {}", id);

        // 아이디 중복 확인하기
        memberService.confirmingDuplicatedId(id);

        // 아이디 사용 가능
        String usingPossibleId = "사용 가능 아이디";
        Map<String, String> resultMap = new HashMap<>();

        resultMap.put("result", usingPossibleId);

        return resultMap;
    }



}
