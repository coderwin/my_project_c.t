package hj.board.ct.web.board;

import hj.board.ct.domain.UploadFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 게시글 수정에서 데이터 읽어 올 때 form
 */
@Getter @Setter
public class BoardUpdateReadForm {

    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private List<UploadFile> uploadFileList; // 첨부파일 모음
    private String rock; // 비밀글 설정

    // ** 생성 로직 ** //
    public static BoardUpdateReadForm createBoardUpdateReadForm(
            String title,
            String content,
            List<UploadFile> uploadFileList,
            String rock) {

        BoardUpdateReadForm boardUpdateReadForm = new BoardUpdateReadForm();

        boardUpdateReadForm.setTitle(title);
        boardUpdateReadForm.setContent(content);
        boardUpdateReadForm.setUploadFileList(uploadFileList);
        boardUpdateReadForm.setRock(rock);

        return boardUpdateReadForm;
    }

}
