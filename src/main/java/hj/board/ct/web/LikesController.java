package hj.board.ct.web;

import hj.board.ct.service.LikesService;
import hj.board.ct.util.SessionConst;
import hj.board.ct.web.member.login.LoginMemberFormInSession;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board/likes")
public class LikesController {

    private final LikesService likesService;

    /**
     * /board/list 요청이 올 때, 실행된다
     *  - 0면 heart.png로 나타난다
     *  - 1면 fullheart.png로 나타난다
     */
    @GetMapping
    @ApiOperation(value = "게시글 좋아요 출력")
    public int startLikesImage(Long boardNum, HttpServletRequest request) {

        if(boardNum == null) {
            return 0;
        }
        // session이 있는지 확인
        HttpSession session = request.getSession(false);
        if(session == null) {
            // heart.png로 나타난다.
            return 0;
        } else if(session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            // heart.png로 나타난다.
            return 0;
        } else {
            log.info("boardNum : ", boardNum);
            // 좋아요의 이미지를 결정한다.
            return likesService.determineInitImage(boardNum, session);
        }
    }

    /**
     * likes 추가하기
     */
    @PostMapping
    @ApiOperation(value = "게시글 좋아요 저장")
    public void createLikes(Long boardNum,
                           @SessionAttribute(
                                   name = SessionConst.LOGIN_MEMBER,
                                   required = false
                           ) LoginMemberFormInSession loginMemberFormInSession) {
        // session이 없으면 error를 던진다
        if(loginMemberFormInSession == null) {
            log.info("error 발생");
            throw new IllegalStateException("로그인 후 이용해주세요.");
        } else {
            log.info("likes/board/save 왔다");
            log.info("likes/board/save 왔다 boardNum : {}", boardNum);
            likesService.saveLikes(boardNum, loginMemberFormInSession.getMemberId());
        }
    }

    /**
     * likes 삭제하기
     */
    @DeleteMapping
    @ApiOperation(value = "게시글 좋아요 삭제")
    public void deleteLikes(Long boardNum,
                           @SessionAttribute(
                                   name = SessionConst.LOGIN_MEMBER,
                                   required = false)
                           LoginMemberFormInSession loginMemberFormInSession) {

        // session이 없으면 error를 던진다
        if(loginMemberFormInSession == null) {
            throw new IllegalStateException("로그인 후 이용해주세요.");
        } else {
            log.info("/likes/board/delete boardNum : {}", boardNum);
            likesService.deleteLikes(boardNum, loginMemberFormInSession.getMemberId());
        }
    }



}
