package hj.board.ct.web;

import hj.board.ct.domain.Member;
import hj.board.ct.exception.WithdrawingMemberException;
import hj.board.ct.service.LoginService;
import hj.board.ct.util.CookieManager;
import hj.board.ct.util.SessionConst;
import hj.board.ct.web.member.login.LoginForm;
import hj.board.ct.web.member.login.LoginMemberFormInSession;
import hj.board.ct.web.member.login.LoginSearchIdForm;
import hj.board.ct.web.member.login.LoginSearchPwdForm;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 로그인 시도 처리 controller
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberLoginController {

    private final LoginService loginService;

    /**
     * 회원 로그인 page 접속
     */
    @GetMapping(value = "/login")
    @ApiOperation(value = "로그인 페이지 이동")
    public String loginForm(@ModelAttribute LoginForm loginForm,
                            @CookieValue(required = false, name = CookieManager.REMEMBER_ID) String rememberId,
                            Model model
                            ) {
        log.info("/login rememberId {}", rememberId);
        if(rememberId != null) {
            // 아이디 던져주기
            loginForm.setLoginId(rememberId);
            loginForm.setRememberId(true);
        }

        return "login/loginForm";
    }

    /**
     * 회원 로그인 처리
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "로그인 처리")
    public String login(@ModelAttribute LoginForm form, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/board/list") String requestURL,
                        HttpServletRequest request,
                        HttpServletResponse response
                        ) {
//        if(bindingResult.hasErrors()) {
//            return "login/loginForm";
//        }
        log.info("/login LoginForm : {}", form);

        // 아이디 존재 확인
        Member loginMember = null;
        try {

            loginMember = loginService.confirmLogin(form.getLoginId(), form.getLoginPwd());

            // 탈퇴회원이면 예외 발생
        } catch (WithdrawingMemberException e) {
            log.error("exception e : ", e);

            // global error 보낸다
            bindingResult.reject("loginFailForWithdrawal");
            // login page로 이동하기
            return "login/loginForm";
        }

        // 로그인 실패
        // login page 으로 이동하기
        if(loginMember == null) {
            // field 상관 없이
            bindingResult.reject("loginFail");
            return "login/loginForm";
        }

        // 로그인 성공
        // session에 기본정보 등록하기
        LoginMemberFormInSession loginMemberFormInSession = LoginMemberFormInSession.createLoginMemberFormInSession(
                loginMember.getMemberNum(),
                loginMember.getMemberId(),
                loginMember.getMemberName());
        HttpSession session = request.getSession(true);

        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMemberFormInSession);

        // 아이디 기억 클릭했는지 확인
        if(form.getRememberId()) {
            // 아이디 cookie에 저장
            CookieManager.makeCookie(
                    CookieManager.REMEMBER_ID,
                    loginMemberFormInSession.getMemberId(),
                    response,
                    86400);
        } else {
            // 아이디 쿠키에서 제거
            CookieManager.expireCookie(CookieManager.REMEMBER_ID, response);
        }

        // 로그인 성공 후 page로 이동 또는 로그인을 시도한 곳으로 이동
        return "redirect:" + requestURL;
    }

    /**
     * 로그아웃 처리
     */
    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃")
    public String logOut(HttpServletRequest request) {
        // 세션 있으면 생성
        HttpSession session = request.getSession(false);
        if(session != null) {
            // 세션에 있는 값들 clear
            session.invalidate();
        }

        return "redirect:/board/list";
    }

    ///////// ******** 아이디 찾기, 비밀번호 찾기   *******///////////////
    /**
     * 아이디 찾기 page 이동
     */
    @GetMapping("/lookforid")
    @ApiOperation(value = "아이디 찾기 페이지 이동")
    public String lookforIdForm(@ModelAttribute LoginSearchIdForm loginSearchIdForm) {
        return "login/loginSearchIdForm";
    }

    /**
     * 아이디 찾기
     */
    @PostMapping("/lookforid/seek")
    @ApiOperation(value = "아이디 찾기")
    public String lookforId(@ModelAttribute LoginSearchIdForm loginSearchIdForm,
                            Model model) {
        // 아이디 찾기
        List<String> idList = loginService.searchId(loginSearchIdForm);

//        log.info("/lookforid/seek idList : {}", idList);
//        log.info("/lookforid/seek loginSearchIdForm : {}", loginSearchIdForm);

        // 찾는 값이 없을 때
        if(idList.size() == 0) {
            String printIdList = "아이디가 존재하지 않습니다.";
            model.addAttribute("printIdList", printIdList);
            model.addAttribute("inputName", loginSearchIdForm.getName()); // 입력 이름
            return "login/loginSearchIdResult";
        }

        // 아이디의 length에 따라 *로 표시
        List<String> printIdList = new ArrayList<>();

        // 치환되는 아이디
        hideRealId(idList, printIdList);

        log.info("/lookforid/seek printIdList : {}", printIdList);

        // 전달하기
        model.addAttribute("printIdList", printIdList);
        model.addAttribute("inputName", loginSearchIdForm.getName()); // 입력 이름

        return "login/loginSearchIdResult";
    }

    // 치환되는 아이디 - 일정 id 정보를 숨긴다
    private void hideRealId(List<String> idList, List<String> printIdList) {

        idList.forEach((id) -> {
            StringBuffer sb = new StringBuffer();
            sb.append(id);

            // 변수 정하기
            int start = 0;// 치환 시작 site
            int end = 0;// 치환 끝 앞 site
            String printId = "";// 치환된 id

            // 아이디가 1~3 length 일 때 - 1나만 *
            if(id.length() <=3 ) {
                start = id.length() - 1;
                end = id.length() + 1;

                printId = sb.replace(start, end, "*").toString();
            // 아이디가 4~5 length 일 때 - 2나만 **
            } else if(id.length() < 6) {
                start = id.length() - 2;
                end = id.length() + 1;

                printId = sb.replace(start, end, "**").toString();
            // 아이디가 6~7 length 일 때 - 3나만 **
            } else if(id.length() < 8) {
                start = id.length() - 3;
                end = id.length() + 1;

                printId = sb.replace(start, end, "***").toString();
            // 그외 - 4나만 ***
            } else {
                start = id.length() - 4;
                end = id.length() + 1;

                printId = sb.replace(start, end, "****").toString();
            }

            printIdList.add(printId);
        });
    }


    /**
     * 비밀번호 찾기 page 이동
     */
    @GetMapping("/lookforpwd")
    @ApiOperation(value = "비밀번호 찾기 페이지 이동")
    public String lookforPwdForm(@ModelAttribute LoginSearchPwdForm loginSearchPwdForm) {

        return "login/loginSearchPwdForm";
    }

    /**
     * 비밀번호 찾기
     */
    @PostMapping("/lookforpwd/seek")
    @ApiOperation(value = "비밀번호 찾기")
    public String lookforPwd(@ModelAttribute LoginSearchPwdForm loginSearchPwdForm,
                            Model model) {
        // 비밀번호 찾기
        List<String> pwdList = loginService.searchPwd(loginSearchPwdForm);

        log.info("/lookforid/seek pwdList: {}", pwdList);
        log.info("/lookforid/seek loginSearchIdForm : {}", loginSearchPwdForm);

        // 찾는 값이 없을 때
        if(pwdList.size() == 0) {
            String printPwdList = "잘못된 정보를 입력하셨습니다.";
            model.addAttribute("printPwdList", printPwdList);
            model.addAttribute("inputId", loginSearchPwdForm.getId());// 입력 아이디
            return "login/loginSearchPwdResult";
        }

        // 아이디 뒤에 4자리는 ****로 표시
        // 비밀번호 담을 list 생성
        List<String> printPwdList = new ArrayList<>();

        // 비밀번호 list에 담기
        pwdList.forEach((pwd) -> {
            StringBuffer sb = new StringBuffer();

            sb.append(pwd);
            int start = pwd.length() - 4;
            int end = pwd.length() + 1;

            log.info("pwd {}", pwd);

            String printPwd = sb.replace(start, end, "****").toString();

            log.info("printId {}", printPwd);

            printPwdList.add(printPwd);
        });

        log.info("/lookforid/seek printPwdList : {}", printPwdList);

        // 전달하기
        model.addAttribute("printPwdList", printPwdList);
        model.addAttribute("inputId", loginSearchPwdForm.getId());// 입력 아이디

        return "login/loginSearchPwdResult";
    }

}
