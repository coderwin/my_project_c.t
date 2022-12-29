package hj.board.ct.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
//@DynamicInsert
//@DynamicUpdate
//@ToString
// 게시글 작성
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_num")
    private Integer num;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자 id

    @Column(length = 200)
    private String boardTitle; // 게시글 제목
    @Column(length = 10000)
    private String boardContent; // 게시글 내용
    private LocalDateTime boardWritingdate; // 게시글 작성 날짜
    private LocalDateTime boardUpdatedate; // 게시글 수정 날짜
    @ColumnDefault("0")
    private Long boardHits; // 조회수
    @Column(length = 50)
    private String boardRock; // 비밀글 설정

    // 삭제하면 UploadFile 테이블의 데이터도 사라진다
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<UploadFile> uploadFileList = new ArrayList<>();// 여러개 이미지 파일 모음
                                             // 원본 파일 이름/ 저장된 파일 이름이 있다
    // 게시글 좋아요
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Likes> likesList;

    // 게시글 댓글
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @OrderBy(value = "reply_num DESC")
    private List<Reply> replyList;

    // == 생성 메서드 == //
    public static Board createBoard(Member member,
                                    String title,
                                    String content,
                                    String boardRock) {

        Board board = new Board();

        board.setMember(member);
        board.setBoardTitle(title);
        board.setBoardContent(content);
        board.setBoardWritingdate(LocalDateTime.now());
        board.setBoardUpdatedate(LocalDateTime.now());
        board.setBoardRock(boardRock);

        return board;
    }

    /**
     * 줄바꿈 <br/>로 치환 createBoard
     * - 입력글자 수에 차이가 나지 않을까(max length = 10000)
     */
    public static Board createBoardV2(Member member,
                                    String title,
                                    String content,
                                    String boardRock) {

        Board board = new Board();

        board.setMember(member);
        board.setBoardTitle(title);
        board.setBoardContent(showSpaceAndLineBreakInContent(content));
        board.setBoardWritingdate(LocalDateTime.now());
        board.setBoardUpdatedate(LocalDateTime.now());
        board.setBoardRock(boardRock);

        return board;
    }

    // == 비스니스 로직 == //
    /**
     * 게시글 컨텐츠 줄바꿈으로 <br />로 바꾸기
     */
    private static String showSpaceAndLineBreakInContent(String content) {
        String result = "";// 치환 결과 담기
        // 줄바꿈 처리
        result = content.replaceAll("(\\n|\\r\\n|\\r)", "<br/>");

        return result;
    }

    // 조회수 default 정하기
    @PrePersist
    public void prePersist() {
        this.boardHits = this.boardHits == null ? 0 : this.boardHits; // hits의 기본값/원래값 정해진다
    }









}
