package hj.board.ct.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 게시글 좋아요
 */
@Entity
@Getter @Setter
public class Likes {

    @Id @GeneratedValue
    @Column(name = "likes_num")
    private Integer num; // 좋아요 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 좋아요 클릭 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_num")
    private Board board; // 게시글 번호

    // ** 연관관계 메서드 ** //
    public void addMember(Member member) {
        member.getLikesList().add(this);
        this.setMember(member);
    }

    // ** 생성 로직 ** //
    public static Likes createLikes(Member member, Board board) {

        Likes likes = new Likes();

        likes.addMember(member);
        board.addLikes(likes);

        return likes;
    }

}
