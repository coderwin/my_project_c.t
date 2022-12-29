package hj.board.ct.web.board;

import hj.board.ct.domain.Likes;
import hj.board.ct.domain.Reply;
import hj.board.ct.domain.UploadFile;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 화면 view에 사용 될 게시글 form
 */
@Getter @Setter
public class BoardViewForm {

    private Long num;// 게시글 번호
    private String memberId; // 작성자 id
    private String boardTitle; // 게시글 제목
    private String boardContent; // 게시글 내용
    private LocalDateTime boardWritingdate; // 게시글 작성 날짜
    private LocalDateTime boardUpdatedate; // 게시글 수정 날짜
    private String boardRock; // 비밀글 설정
    private Long hits; // 조회수

    private List<UploadFile> uploadFileList; // 게시글 첨부파일
    private List<Reply> replyList; // 댓글
    private List<Likes> likesList = new ArrayList<>(); // 좋아요


    // ** 생성 메서드 ** //
    public static BoardViewForm createBoardForm(
            Long num,
            String id,
            String title,
            String content,
            LocalDateTime writingdate,
            LocalDateTime updatedate,
            String boardRock,
            List<UploadFile> uploadFileList
            ) {

        BoardViewForm boardViewForm = new BoardViewForm();

        boardViewForm.setNum(num);
        boardViewForm.setMemberId(id);
        boardViewForm.setBoardTitle(title);
        boardViewForm.setBoardContent(content);
        boardViewForm.setBoardWritingdate(writingdate);
        boardViewForm.setBoardUpdatedate(updatedate);
        boardViewForm.setUploadFileList(uploadFileList);
        boardViewForm.setBoardRock(boardRock);

        return boardViewForm;
    }

    // BoardViewForm 생성 -- boardService에 사용
    public static BoardViewForm createBoardForm(
            Long num,
            String id,
            String title,
            String content,
            LocalDateTime writingdate,
            LocalDateTime updatedate,
            String boardRock,
            List<UploadFile> uploadFileList,
            Long hits,
            List<Reply> replyList,
            List<Likes> likesList) {

        BoardViewForm boardViewForm = new BoardViewForm();

        boardViewForm.setNum(num);
        boardViewForm.setMemberId(id);
        boardViewForm.setBoardTitle(title);
        boardViewForm.setBoardContent(content);
        boardViewForm.setBoardWritingdate(writingdate);
        boardViewForm.setBoardUpdatedate(updatedate);
        boardViewForm.setUploadFileList(uploadFileList);
        boardViewForm.setBoardRock(boardRock);
        boardViewForm.setHits(hits);
        boardViewForm.setReplyList(replyList);
        boardViewForm.setLikesList(likesList);

        return boardViewForm;
    }

    // ** 비즈니스 로직 ** //

    // uploadFileList의 확자자 조사 => 이미지 파일만 보관하기
    public static List<UploadFile> checkExe(List<UploadFile> uploadFiles) {
        // 확장자 pattern 설정
        String pattern = ".*(?<=\\.(jpg|JPG|png|PNG|jpeg|JPEG|gif|GIF))";
        // pattern으로 파일 필터
        List<UploadFile> findUploadFiles = uploadFiles
                .stream()
                .filter((uploadFile) ->
                    uploadFile.getStoreFileName().matches(pattern)
                )
                .collect(Collectors.toList());

        return findUploadFiles;
    }




}
