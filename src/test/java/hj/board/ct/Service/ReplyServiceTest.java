package hj.board.ct.Service;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import hj.board.ct.domain.Reply;
import hj.board.ct.repository.BoardRepository;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.repository.ReplyRepository;
import hj.board.ct.service.ReplyService;
import hj.board.ct.web.reply.ReplyForm;
import hj.board.ct.web.reply.ReplyUpdateForm;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class ReplyServiceTest {

    @Autowired
    ReplyService replyService;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    EntityManager em;

    /**
     * 댓글 저장하기 확인
     *  - test 개선 필요!!
     */
    @Test
//    @Rollback(value = false)
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

        // replyForm 만들기
        ReplyForm replyForm = new ReplyForm();
        replyForm.setContent("hello~");
        replyForm.setRock("ladf1123");

        // when
        // reply 저장하기
        replyService.saveReply(replyForm, board.getNum(), member.getMemberId());

//        em.flush();
//        em.clear();

        // then
        Reply findReply = replyRepository.findByNum(5L); // test 부족

        // 데이터로 확인하기
        log.info("findReply : {}", findReply);

        assertThat(findReply.getReplyContent()).isEqualTo(replyForm.getContent());
        assertThat(findReply.getReplyRock()).isEqualTo(replyForm.getRock());
        assertThat(findReply.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findReply.getBoard().getNum()).isEqualTo(board.getNum());
//        assertThat(findReply.getBoard().getNum()).isEqualTo(1L);

    }

    /**
     * 댓글 수정하기 확인
     */
    @Test
    public void 수정하기() {

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

        // reply 저장
        Reply reply = Reply.createReply(content, rock, member, board);

        replyRepository.save(reply);

        log.info("저장 후 reply info : {}", reply);

        // when
        // 수정하기
        ReplyUpdateForm replyUpdateForm = new ReplyUpdateForm();
        replyUpdateForm.setContent("안녕하세요");
        replyUpdateForm.setRock("5698#@");

        replyService.updateReply(reply.getNum(), replyUpdateForm);

        // then
        // reply 불러오기
        Reply findReply = replyRepository.findByNum(reply.getNum());

        // 값을 확인하기
        assertThat(findReply.getReplyContent()).isEqualTo(reply.getReplyContent());
        assertThat(findReply.getReplyRock()).isEqualTo(reply.getReplyRock());
        assertThat(findReply.getReplyUpdatedate()).isEqualTo(reply.getReplyUpdatedate());
        assertThat(findReply.getReplyUpdatedate()).isNotEqualTo(reply.getReplyWritingdate());
//        assertThat(findReply.getReplyUpdatedate()).isEqualTo(reply.getReplyWritingdate());
    }

    /**
     * 댓글 삭제하기 확인
     */
    @Test
    public void 댓글_삭제하기() {
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

        // reply 저장
        Reply reply = Reply.createReply(content, rock, member, board);

        replyRepository.save(reply);

        // when
        // 삭제하기
        replyService.deleteReply(reply.getNum());

        // then
        Reply findReply = replyRepository.findByNum(reply.getNum());

        assertThat(findReply).isEqualTo(null);
//        assertThat(findReply).isEqualTo(reply);
    }








}
