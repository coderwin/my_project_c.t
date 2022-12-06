package hj.board.ct.web.board;

import hj.board.ct.domain.Member;
import hj.board.ct.domain.UploadFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 수정시 필요한 form
 */
@Getter @Setter
public class BoardUpdateForm {

    @NotBlank(message = "비어있을 수 없습니다.")
    private String title; // 게시글 제목

    @NotBlank(message = "비어있을 수 없습니다.")
    private String content; // 게시글 내용

    private List<UploadFile> uploadFileList; // 기존 첨부파일

    private List<MultipartFile> multipartFileList; // 새로 생성된 첨부파일 모음
    private String rock; // 비밀글 설정

    public BoardUpdateForm() {
    }

    // test에 사용
    public BoardUpdateForm(String title, String content, String rock) {
        this.title = title;
        this.content = content;
        this.rock = rock;
    }

    // ** 생성 로직 ** //
    public static BoardUpdateForm createBoardUpdateForm(
            String title,
            String content,
            String rock,
            List<UploadFile> uploadFileList) {

        BoardUpdateForm boardUpdateForm = new BoardUpdateForm();

        boardUpdateForm.setTitle(title);
        boardUpdateForm.setContent(content);
        boardUpdateForm.setRock(rock);
        boardUpdateForm.setUploadFileList(uploadFileList);

        return boardUpdateForm;
    }


}
