package hj.board.ct.repository;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;

    /**
     * 게시글 저장 확인
     */
    @Test
//    @Rollback(value = false)
    public void 게시글_저장() {
        // given
        Member member = Member.createMember("test9",
                "1234",
                "name",
                "test@test",
                "19990202", MemberChecking.Y, MemberChecking.N,
                "010-1234",
                "051-123");

        memberRepository.save(member);

        Board board = Board.createBoard(member, "hello", "hi", "123!@");

        // when
        boardRepository.save(board);

        // then
        // board 찾기
        Board findBoard = boardRepository.findByNum(board.getNum()).orElseThrow();

        assertThat(findBoard).isEqualTo(board);
    }

    /**
     * 모든 게시글 불러오기 확인
     */
    @Test
//    @Rollback(value = false)
    public void 게시글_모두_가져오기() {

        // given
        Member member1 = memberRepository.findById("test1").orElseThrow();
        Member member2 = memberRepository.findById("test12").orElseThrow();

        Board board1 = Board.createBoard(member1, "hello123", "hi123", "123!@");
        Board board2 = Board.createBoard(member2, "hello12", "hi12", null);
        boardRepository.save(board1);
        boardRepository.save(board2);

        // when // then
        // 1) 아무 조건도 없을 때
        testFindAll("", "", "", "", board1, board2);
        // 2) id 있을 때
        testFindAll("12", "", "", "", board2);// expect board2
        // 3) title 있을 때
        testFindAll("", "123", "", "", board1);
        // 4) content 있을 때
        testFindAll("", "", "hi", "", board1, board2);
        // 5) writingdate 있을 때
        testFindAll("", "", "", "20221021", board1, board2);
        // 6) id, title 있을 때
        testFindAll("test", "123", "", "", board1);
        // 7) id, content 있을 때
        testFindAll("test", "", "123", "", board1);
        // 8) id, writingdate 있을 때
        testFindAll("test", "", "", "20221017");
        // 9) id, title, content 있을 때
        testFindAll("test", "12", "", "", board1, board2);
        // 10) id, title, writingdate 있을 때
        testFindAll("test", "123", "", "20221021", board1);
        // 11) id, content, writingdate 있을 때
        testFindAll("2", "", "12", "20221021", board2);
        // 12) id, title, content, writingdate 있을 때
        testFindAll("2", "12", "hi", "20221021", board2);
    }

    // 게시글_모두_가져오기에 필요한 when, then 부분
    private void testFindAll(String id, String title, String content, String writingdate, Board... boards) {
        // 게시글 모두 가져오기
        BoardSearchCond boardSearchCond = new BoardSearchCond(id, title, content, writingdate);

        List<Board> boardList = boardRepository.findAll(boardSearchCond);

        assertThat(boardList).containsExactly(boards);
    }

    /**
     * 게시글 삭제 확인
     */
    @Test
    public void 게시글_삭제하기() {
        // given
        // Member 생성
        Member member = memberRepository.findById("test1").orElseThrow();

        // board 생성
        Board board = Board.createBoard(member, "hello123", "hi123", "123!@");
        boardRepository.save(board);

        // when
        // 삭제하기
        boardRepository.delete(board);

        // then
        // 삭제한 board가 정말 삭제되었는지 확인하기
        log.info("board num : {}", board.getNum());

        assertThrows(NoSuchElementException.class, () -> {
            boardRepository.findByNum(board.getNum()).orElseThrow();
        });
    }

    /**
     * 게시글 불러오기_1개 확인
     */
    @Test
    public void 게시글_불러오기_1개() {
        // given
        // 멤버를 데려오기
        Member member = memberRepository.findById("test1").orElse(null);

        // board 저장하기
        Board board = Board.createBoard(member, "hello123", "hi123", "123!@");

        boardRepository.save(board);
        // when
        // 게시물 가져오기
        Board findBoard = boardRepository.findByNum(board.getNum()).orElse(null);
        // then
        assertThat(findBoard).isEqualTo(board);
    }

    /**
     * 게시글 불러오기 by member_id 확인
     */
    @Test
    public void 게시글_가져오기_byMemberId() {
        // given
        // 멤버를 데려오기
        Member member = memberRepository.findById("test1").orElseThrow();
        // board 2개 생성하기
        Board board1 = Board.createBoard(member, "hello123", "hi123", "123!@");
        Board board2 = Board.createBoard(member, "hello123", "hi123", "123!@");

        boardRepository.save(board1);
        boardRepository.save(board2);

        // when
        // 멤버가 생성한 board 가져오기
        List<Board> boardList = boardRepository.findAllById(member.getMemberId());

        // then
        // 먼저 생성한 board가 다 나왔는지 확인하기
        assertThat(boardList).containsExactly(board1, board2);
    }

    /**
     * 게시글 불러오기 join을 이용하기 확인
     */
    @Test
    public void 게시글_가져오기_join_이용하여() {
        // given
        // 멤버를 데려오기
        Member member1 = memberRepository.findById("test1").orElseThrow();
        Member member2 = memberRepository.findById("test12").orElseThrow();


        // board 2개 생성하기
        Board board1 = Board.createBoard(member1, "hello123", "hi123", "123!@");
        Board board2 = Board.createBoard(member1, "hello123", "hi123", "123!@");
        Board board3 = Board.createBoard(member1, "hello123", "hi123", "123!@");
        Board board4 = Board.createBoard(member2, "hello123", "hi123", "123!@");

        boardRepository.save(board1);
        boardRepository.save(board2);
        boardRepository.save(board3);
        boardRepository.save(board4);

        // when
        // 멤버가 생성한 board 가져오기
        List<Board> boardListMadebyMember1 = boardRepository.findAllByMember(member1.getMemberId());
        List<Board> boardListMadebyMember2 = boardRepository.findAllByMember(member2.getMemberId());

        // then
        // 먼저 생성한 board가 다 나왔는지 확인하기
        assertThat(boardListMadebyMember1).containsExactly(board1, board2, board3);
        assertThat(boardListMadebyMember2).containsExactly(board4);

    }


}
