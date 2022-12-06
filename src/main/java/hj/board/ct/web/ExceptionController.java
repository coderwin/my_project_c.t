package hj.board.ct.web;

import hj.board.ct.web.board.BoardForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * 예외를 처리해준다
 * - 파일 업로드 초과 예외 처리
 * - 현재 사용 못 함/ 예외 처리가 안 됨
 */
//@ControllerAdvice
@Slf4j
public class ExceptionController {


//    /**
//     * 파일 업로드 시, 용량초과 예외 처리V1
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler({FileSizeLimitExceededException.class, SizeLimitExceededException.class, MaxUploadSizeExceededException.class})
//    public String fileCapacityExceededExHandle(
//                                               Exception e,
//                                               @ModelAttribute BoardForm boardForm,
//                                               BindingResult bindingResult
//                                                ) {
//
//        log.info("fileCapacityExceededExHandle 여기 들립니다.");
//        log.error("exception appears : ", e);
//
//        // 파일 용량 초과 예외 처리
//        log.info("fileupload size exceeded : ", e);
//        bindingResult.rejectValue("multipartFileList", "capacityExceeded", null);
//
//        log.info("/write bindingResult : {}", bindingResult);
//        // 본래 페이지로 돌아가기
//        return "boards/createBoardForm";
//    }

//    /**
//     * 파일 업로드 시, 용량초과 예외 처리V2
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler({FileSizeLimitExceededException.class, SizeLimitExceededException.class, MaxUploadSizeExceededException.class})
//    public ModelAndView fileCapacityExceededExHandle(
//            Exception e,
//            @ModelAttribute BoardForm boardForm,
//            ModelAndView modelAndView,
//            BindingResult bindingResult
//    ) {
//
//        log.info("fileCapacityExceededExHandle 여기 들립니다.");
//        log.error("exception appears : ", e);
//
//        // 파일 용량 초과 예외 처리
//        log.info("fileupload size exceeded : ", e);
//        bindingResult.rejectValue("multipartFileList", "capacityExceeded", null);
//
//        log.info("/write bindingResult : {}", bindingResult);
//        // 본래 페이지로 돌아가기
//        modelAndView.setViewName("boards/createBoardForm");
//
//        return modelAndView;
//    }

    /**
     * 파일 업로드 시, 용량초과 예외 처리V2
     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(FileSizeLimitExceededException.class)
//    public ModelAndView fileCapacityExceededExHandle(
//            FileSizeLimitExceededException e
//    ) {
//
//        ModelAndView modelAndView = new ModelAndView();
//        log.info("fileCapacityExceededExHandle 여기 들립니다.");
//        log.error("exception appears : ", e);
//
//        // 파일 용량 초과 예외 처리
//        log.info("fileupload size exceeded : ", e);
////        bindingResult.rejectValue("multipartFileList", "capacityExceeded", null);
////
////        log.info("/write bindingResult : {}", bindingResult);
//        // 본래 페이지로 돌아가기
//        modelAndView.setViewName("boards/createBoardForm");
//
//        return modelAndView;
//    }

    /**
     * 파일 업로드 시, 용량초과 예외 처리V2
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public String fileCapacityExceededExHandle(
            FileSizeLimitExceededException e
    ) {

        log.info("fileCapacityExceededExHandle 여기 들립니다.");
        log.error("exception appears : ", e);

        // 파일 용량 초과 예외 처리
//        bindingResult.rejectValue("multipartFileList", "capacityExceeded", null);
//
//        log.info("/write bindingResult : {}", bindingResult);
        // 본래 페이지로 돌아가기
        return "boards/createBoardForm";
    }

    /**
     * 파일 업로드 시, 용량초과 예외 처리V2
     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(SizeLimitExceededException.class)
//    public ModelAndView fileCapacityExceededExHandle(
//            SizeLimitExceededException e
//    ) {
//
//        ModelAndView modelAndView = new ModelAndView();
//        log.info("fileCapacityExceededExHandle 여기 들립니다.");
//        log.error("exception appears : ", e);
//
//        // 파일 용량 초과 예외 처리
////        log.info("fileupload size exceeded : ", e);
////        bindingResult.rejectValue("multipartFileList", "capacityExceeded", null);
////
////        log.info("/write bindingResult : {}", bindingResult);
//        // 본래 페이지로 돌아가기
//        modelAndView.setViewName("boards/createBoardForm");
//
//        return modelAndView;
//    }

    /**
     * 파일 업로드 시, 용량초과 예외 처리V2
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SizeLimitExceededException.class)
    public String fileCapacityExceededExHandle(
            SizeLimitExceededException e
    ) {

        log.info("fileCapacityExceededExHandle 여기 들립니다.");
        log.error("exception appears : ", e);

//        BindingResult bindingResult = BindingResult.
        //파일 용량 초과 예외 처리
//        bindingResult.rejectValue("multipartFileList", "capacityExceeded", null);

//        log.info("/write bindingResult : {}", bindingResult);
        // 본래 페이지로 돌아가기
        return "redirect:/board/write";
    }

    /**
     * 파일 업로드 시, 용량초과 예외 처리V2
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String fileCapacityExceededExHandle(
            MaxUploadSizeExceededException e
    ) {

        log.info("fileCapacityExceededExHandle 여기 들립니다.");
        log.error("exception appears : ", e);

//        BindingResult bindingResult = BindingResult.
        //파일 용량 초과 예외 처리
//        bindingResult.rejectValue("multipartFileList", "capacityExceeded", null);

//        log.info("/write bindingResult : {}", bindingResult);
        // 본래 페이지로 돌아가기
        return "redirect:/board/write";
    }

}
