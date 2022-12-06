package hj.board.ct.web;

import hj.board.ct.domain.Reply;
import hj.board.ct.repository.ReplyRepository;
import hj.board.ct.service.ReplyService;
import hj.board.ct.util.SessionConst;
import hj.board.ct.web.member.login.LoginMemberFormInSession;
import hj.board.ct.web.reply.ReplyForm;
import hj.board.ct.web.reply.ReplyUpdateForm;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static hj.board.ct.domain.QReply.reply;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;
    private final ReplyRepository replyRepository;

    /**
     * 댓글 저장하기
     */
    @PostMapping("/reply/board/{boardNum}/write")
    @ApiOperation(value = "댓글 등록")
    public void createReply(
            @PathVariable Long boardNum,
            @RequestBody ReplyForm replyForm,
            @SessionAttribute(
                    name = SessionConst.LOGIN_MEMBER,
                    required = false) LoginMemberFormInSession loginMemberFormInSession) {

        // 필요 데이터 보내주기
        replyService.saveReply(replyForm, boardNum, loginMemberFormInSession.getMemberId());
    }

    /**
     * 댓글 수정하기
     */
    @PostMapping("/reply/board/{replyNum}/edit")
    @ApiOperation(value = "댓글 수정")
    public void updateReply(@PathVariable Long replyNum,
                            @RequestBody ReplyUpdateForm replyUpdateForm) {

        replyService.updateReply(replyNum, replyUpdateForm);
    }

    /**
     * 댓글 삭제하기
     */
    @PostMapping("/reply/board/{replyNum}/delete")
    @ApiOperation(value = "댓글 삭제")
    public void deleteReply(@PathVariable Long replyNum) {

        replyService.deleteReply(replyNum);
    }

}
