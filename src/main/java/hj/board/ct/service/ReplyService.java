package hj.board.ct.service;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.Member;
import hj.board.ct.domain.Reply;
import hj.board.ct.repository.BoardRepository;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.repository.ReplyRepository;
import hj.board.ct.web.reply.ReplyForm;
import hj.board.ct.web.reply.ReplyUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    /**
     * 저장하기
     */
    @Transactional
    public void saveReply(ReplyForm replyForm, Integer boardNum, String memberId) {

        // member 불러오기
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
            new IllegalStateException("존재하지 않는 사용자입니다.")
        );

        // board 불러오기
        Board board = boardRepository.findByNum(boardNum).orElseThrow(() ->
            new IllegalStateException("존재하지 않는 게시글입니다.")
        );

        if(member != null & board != null) {
            // reply 생성하기
            Reply reply = Reply.createReply(replyForm.getContent(), replyForm.getRock(), member, board);

            // reply 저장하기
            replyRepository.save(reply);
        }
    }

    /**
     * 수정하기
     */
    @Transactional
    public void updateReply(Integer replyNum, ReplyUpdateForm replyUpdateForm) {
        // reply 불러오기
        Reply findReply = replyRepository.findByNum(replyNum);

        // 데이터 수정하기
        findReply.setReplyContent(replyUpdateForm.getContent());
        findReply.setReplyRock(replyUpdateForm.getRock());
        findReply.setReplyUpdatedate(LocalDateTime.now());
    }

    @Transactional
    public void deleteReply(Integer replyNum) {

        Reply findReply = replyRepository.findByNum(replyNum);

        replyRepository.delete(findReply);
    }
}
