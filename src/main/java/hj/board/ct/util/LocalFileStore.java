package hj.board.ct.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 로컬에 저장되어 있는 파일을 보여준다.
 * - 현재 사용 안 함
 */
@Slf4j
//@Component
public class LocalFileStore {

    @Value("${realFile.dir}")
    private String realFileDir; // 절대 경로 at 배포

    /**
     * <img >에 이미지를 보여준다
     * - 배포시에 이미지를 compile
     */
    public ResponseEntity<Resource> getRealImageFile(String fileName) {

        Resource resource = new FileSystemResource(realFileDir + fileName);

        // 파일이 존재 안 하면 404 에러 발생
        if(!resource.exists()) {
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }

        // HTTP Header
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;

        try {
            filePath = Paths.get(realFileDir + fileName); // 파일 경로 만들기
            header.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            log.info("getRealImageFile : ", e);
        }

        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }








}
