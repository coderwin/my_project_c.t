package hj.board.ct.intercetpor;

import hj.board.ct.web.board.BoardForm;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 파일 용량 초과 확인 체크하는 intercepter
 */
@Slf4j
public class FileStorageCheckInterceptor implements HandlerInterceptor {

    @Value("file.StorageCapacity")
    private Long STORAGE_CAPACITY; // 파일 저장 용량
    private Long fileSizes; // 모든 파일 사이즈 합

    /**
     * 파일 용량 초과 확인 체크한다
     */
//    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler,
                             @ModelAttribute BoardForm boardForm,
                             BindingResult bindingResult
    ) throws Exception {

        // 요청 uri 확인
        String requestURI = request.getRequestURI();
        log.info("FileStorageCheckInterceptor preHandle uri : {}", requestURI);

        log.info("파일 용량 초과 체크 인터셉터 실행중... {}", requestURI);

        // 용량 확인
        // 모든 fileSize 합하기
        for(MultipartFile multipartFile : boardForm.getMultipartFileList()) {

            fileSizes += multipartFile.getSize();
        }

        // 초과하면 /write로 보낸다
        if(fileSizes > STORAGE_CAPACITY) {
            log.info("저장 파일 용량 초과 발생");
            // 파일 용량 초과 예외 담기
            bindingResult.rejectValue("multipartFileList", "capacityExceeded", null);
            response.sendRedirect(requestURI);

            return false; // 인터셉터 종료
        }

        // 게시글 저장 Controller로 이동
        return true;
    }
}
