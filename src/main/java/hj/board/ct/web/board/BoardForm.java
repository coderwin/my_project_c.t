package hj.board.ct.web.board;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 작성에 필요한 form
 */
@Getter @Setter
@ToString
@EqualsAndHashCode
public class BoardForm {

    @NotBlank(message = "비어있을 수 없습니다.")
    private String boardTitle; // 게시글 제목
    @NotBlank(message = "비어있을 수 없습니다.")
    private String boardContent; // 게시글 내용

    private List<MultipartFile> multipartFileList; // 첨부파일 모음
    private String boardRock; // 비밀글 설정

    // test에 사용
    public BoardForm(String boardTitle, String boardContent, String boardRock) {

        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardRock = boardRock;
    }

}
