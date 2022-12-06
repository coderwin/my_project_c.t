package hj.board.ct.intercetpor;

import hj.board.ct.util.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 로그인이 된 사용자인지 체크하는 intercepter
 */
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    /**
     * 로그인인 된 사용자인지 체크한다
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        // 요청 uri 확인
        String requestURI = request.getRequestURI();
        log.info("LoginCheckInterceptor preHandle uri : {}", requestURI);

        log.info("인증 체크 인터셉터 실행중... {}", requestURI);
        // session 가져오기
        HttpSession session = request.getSession(false);
        // session 존재유무 확인
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            // 비회원 사용자인 경우
            log.info("비회원 사용자 요청");
            String redirect = "/login?requestURL=" + requestURI;
            response.sendRedirect(redirect);

            return false; // 인터셉터 종료
        }

        // 로그인한 사용자
        return true; //
    }
}
