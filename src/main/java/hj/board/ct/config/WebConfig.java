package hj.board.ct.config;

import hj.board.ct.intercetpor.FileStorageCheckInterceptor;
import hj.board.ct.intercetpor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 로그인 인증 진행 +
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그인 인증 인터셉터 등록
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/index.html", "/login", "/logout", "/join/**",
                        "/css/**", "/js/**", "/*.ico", "/error", "/board/list",
                        "/lookforid/**", "/lookforpwd/**", "/board/{num}/detail", "/images/**", "/board/images/**", "/board/pwd/**",
                        "/board/attach/**"); // /error, "/*.ico"

//        // 파일 저장용량 인터셉터 등록
//        registry.addInterceptor(new FileStorageCheckInterceptor())
//                .order(2)
//                .addPathPatterns("/board/write");
    }




    // argumentResolver 등록/데이터 전달 받는 어노테이션 등록
//    // 디스페쳐 서블릿에서 컨트롤러로 가는중 요청받은 데이터 타입이 맞는지 확인해준다
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
//    }
}
