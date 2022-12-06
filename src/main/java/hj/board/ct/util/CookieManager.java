package hj.board.ct.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 쿠키의 생성, 삭제 등을 다룬다
 * + 쿠키에 사용된는 이름을 모아둔다
 */
public class CookieManager {

    public static final String REMEMBER_ID = "REMEMBER_ID";// 로그인 시, 아이디 기억에 사용되는 쿠키 이름

    /**
     * 쿠키 생성
     */
    public static void makeCookie(String cookieName, String value, HttpServletResponse response, int maxAge) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(86400); // 하루 동안 생성
        // 쿠키 생성
        response.addCookie(cookie);
    }

    /**
     * 쿠키 찾기
     */
    public static Cookie findCookie(String cookieName, HttpServletRequest request) {
        // 쿠키가 없을 때
        if(request.getCookies() == null) {
            return null;
        }

        // 쿠키가 있으면
        Cookie[] cookies = request.getCookies();

        return Arrays.stream(cookies)
                .filter((cookie) ->
                    cookie.getName().equals(cookieName))
                .findFirst()
                .orElse(null);
    }

    /**
     * 쿠키 만료
     */
    public static void expireCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
