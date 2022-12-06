package hj.board.ct.repository;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.Likes;
import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class LikesRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private LikesRepository likesRepository;

    /**
     * 저장하기 확인
     */
    @Test
    public void 저장하기() {
        // given
        // Member 저장하기
        Member member = Member.createMember("test9",
                "1234",
                "name",
                "test@test",
                "19990202", MemberChecking.Y, MemberChecking.N,
                "010-1234",
                "051-123");

        memberRepository.save(member);
        // Board 저장하기
        Board board = Board.createBoard(member, "hello", "hi", "123!@");

        boardRepository.save(board);

        // Likes 만들기
        Likes likes = new Likes();

        likes.setBoard(board);
        likes.setMember(member);

        // when
        // 저장하기
        likesRepository.save(likes);

        // then
        // num으로 찾아오기
        Likes findLikes = likesRepository.findByNum(likes.getNum());

        assertThat(findLikes).isEqualTo(likes);

    }

    /**
     * 삭제하기 확인
     */
    @Test
    public void 삭제하기() {
        // given
        // Member 저장하기
        Member member = Member.createMember("test9",
                "1234",
                "name",
                "test@test",
                "19990202", MemberChecking.Y, MemberChecking.N,
                "010-1234",
                "051-123");

        memberRepository.save(member);
        // Board 저장하기
        Board board = Board.createBoard(member, "hello", "hi", "123!@");

        boardRepository.save(board);

        // Likes 만들기
        Likes likes = new Likes();

        likes.setBoard(board);
        likes.setMember(member);
        // 저장하기
        likesRepository.save(likes);

        // when
        // Likes 삭제하기
        likesRepository.delete(likes);

        // then
        // num으로 찾아보기
        Likes findLikes = likesRepository.findByNum(likes.getNum());

        log.info("likes의 num : {}", likes.getNum());
        log.info("삭제하기 likes : {}", likes);

//        assertThat(findLikes).isEqualTo(likes);
        assertThat(findLikes).isEqualTo(null);

    }





}
