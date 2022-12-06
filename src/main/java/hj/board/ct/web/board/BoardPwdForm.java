package hj.board.ct.web.board;

/**
 * 게시글의 비밀번호를 담는다
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPwdForm {

    private String boardPwd; // 게시글 비밀번호

    //** 생성 로직 **//
    public static BoardPwdForm createBoardPwdForm(String pwd) {
        BoardPwdForm boardPwdForm = new BoardPwdForm();

        boardPwdForm.setBoardPwd(pwd);

        return boardPwdForm;
    }


}
