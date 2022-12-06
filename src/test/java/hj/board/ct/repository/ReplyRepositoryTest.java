package hj.board.ct.repository;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import hj.board.ct.domain.Reply;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    /**
     * 저장하기 확인
     */
    @Test
    public void 저장하기() {
        // given
        // member 저장
        Member member = Member.createMember("test9",
                "1234",
                "name",
                "test@test",
                "19990202", MemberChecking.Y, MemberChecking.N,
                "010-1234",
                "051-123");

        memberRepository.save(member);

        // board 저장
        Board board = Board.createBoard(member, "hello", "hi", "123!@");

        boardRepository.save(board);

        // Reply 만들기
        String content = "hello";
        String rock = "ok123";

        Reply reply = Reply.createReply(content, rock, member, board);

        // when
        // 저장하기
        replyRepository.save(reply);

        // then
        Reply findReply = replyRepository.findByNum(reply.getNum());

        assertThat(findReply).isEqualTo(reply);
    }

    /**
     * 삭제하기 확인
     */
    @Test
    public void 삭제하기() {
        // given
        // member 저장
        Member member = Member.createMember("test9",
                "1234",
                "name",
                "test@test",
                "19990202", MemberChecking.Y, MemberChecking.N,
                "010-1234",
                "051-123");

        memberRepository.save(member);

        // board 저장
        Board board = Board.createBoard(member, "hello", "hi", "123!@");

        boardRepository.save(board);

        // Reply 만들기
        String content = "hello";
        String rock = "ok123";

        Reply reply = Reply.createReply(content, rock, member, board);

        // 저장하기
        replyRepository.save(reply);

        // when
        // 삭제하기
        replyRepository.delete(reply);

        // then
        Reply findReply = replyRepository.findByNum(reply.getNum());
        log.info("reply findReply : {}", findReply);
        log.info("reply : {}", reply);

        assertThat(findReply).isEqualTo(null);
//        assertThat(findReply).isEqualTo(reply);

    }
}
