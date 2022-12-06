package hj.board.ct.web;

import hj.board.ct.util.LocalFileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * LocalImage 불러오는 controller
 * - 현재 사용 안 함
 */

@Slf4j
//@Controller
@RequiredArgsConstructor
public class LocalImageController {

    private final LocalFileStore localFileStore;// 로컬 이미지 외부로 보내준다다

//   /**
//     * 로컬에 저장되어 있는 이미지 파일을 보기
//     * - 배포에 이용
//     */
//    @GetMapping("/image/{savedLocalFileName}")
//    public @ResponseBody ResponseEntity<Resource> getImageInLocal(@PathVariable String savedLocalFileName) {
//
//        log.info("local image 가져오기");
//        return localFileStore.getRealImageFile(savedLocalFileName);
//    }
}
