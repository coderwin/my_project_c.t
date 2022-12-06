package hj.board.ct.web.board;

import hj.board.ct.domain.Likes;
import hj.board.ct.domain.Reply;
import hj.board.ct.domain.UploadFile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 작성에 필요한 form v2 - /board/{num}/detail에 사용
 */
@Getter @Setter
@ToString
@EqualsAndHashCode
public class BoardDetailForm {

    private Long num; // 게시글 번호
    private String id; // 작성자 id
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private LocalDateTime writingdate; // 게시글 작성일
    private Long hits; // 조회수

    private List<UploadFile> uploadFileList = new ArrayList<>();// 첨부파일
    private List<Reply> replyList; // 댓글
    private List<Likes> likesList; // 좋아요

    // **생성 로직** //
    public static BoardDetailForm createBoardDetailForm(
            Long num,
            String id,
            String title,
            String content,
            LocalDateTime writingdate,
            Long hits,
            List<UploadFile> uploadFileList,
            List<Reply> replyList,
            List<Likes> likesList) {

        BoardDetailForm boardDetailForm = new BoardDetailForm();

        boardDetailForm.setNum(num);
        boardDetailForm.setId(id);
        boardDetailForm.setTitle(title);
        boardDetailForm.setContent(content);
        boardDetailForm.setWritingdate(writingdate);
        boardDetailForm.setHits(hits);
        boardDetailForm.setUploadFileList(uploadFileList);
        boardDetailForm.setReplyList(replyList);
        boardDetailForm.setLikesList(likesList);

        return boardDetailForm;
    }

    /**
     * Board의 createBoardV2와 연동한다.
     * - content length = 10000에 의해서 보류
     */
    public static BoardDetailForm createBoardDetailFormV2(
            Long num,
            String id,
            String title,
            String content,
            LocalDateTime writingdate,
            Long hits,
            List<UploadFile> uploadFileList,
            List<Reply> replyList,
            List<Likes> likesList) {

        BoardDetailForm boardDetailForm = new BoardDetailForm();

        boardDetailForm.setNum(num);
        boardDetailForm.setId(id);
        boardDetailForm.setTitle(title);
        boardDetailForm.setContent(showSpaceAndLineBreakInContent(content));
        boardDetailForm.setWritingdate(writingdate);
        boardDetailForm.setHits(hits);
        boardDetailForm.setUploadFileList(uploadFileList);
        boardDetailForm.setReplyList(replyList);
        boardDetailForm.setLikesList(likesList);

        return boardDetailForm;
    }

    /* 비즈니스 로직 */
    /**
     * 게시글 컨텐츠 띄어쓰기 html에 보여주기
     */
    private static String showSpaceAndLineBreakInContent(String content) {
        String result = "";// 치환 결과 담기
        // 띄어쓰기 처리
        result = content.replaceAll(" ", "&nbsp;");

        return result;
    }


}
