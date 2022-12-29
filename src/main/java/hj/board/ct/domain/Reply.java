package hj.board.ct.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

// 게시글 댓글
@Entity
@Getter @Setter
@ToString
public class Reply {

    @Id @GeneratedValue
    @Column(name = "reply_num")
    private Integer num; // 번호
    @Column(length = 5000)
    private String replyContent; // 내용
    private LocalDateTime replyWritingdate; // 작성 날짜
    private LocalDateTime replyUpdatedate; // 수정 날짜
    @Column(length = 30)
    private String replyRock; // 비밀 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_num")
    private Board board; // 게시글

    // ** 생성 로직 ** //
    /**
     * Reply 생성
     */
    public static Reply createReply(String content, String rock, Member member, Board board) {

        Reply reply = new Reply();

        reply.setReplyContent(content);
        reply.setReplyRock(rock);
        reply.setReplyWritingdate(LocalDateTime.now());
        reply.setReplyUpdatedate(LocalDateTime.now());
        reply.setMember(member);
        reply.setBoard(board);

        return reply;
    }



}
