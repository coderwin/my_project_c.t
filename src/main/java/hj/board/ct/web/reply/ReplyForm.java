package hj.board.ct.web.reply;

import lombok.Getter;
import lombok.Setter;

/**
 * 저장에 필요한 replyForm
 */
@Getter @Setter
public class ReplyForm {

    private String content; // 내용
    private String rock; // 비밀번호
}
