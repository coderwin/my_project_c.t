package hj.board.ct.service;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.Likes;
import hj.board.ct.domain.Member;
import hj.board.ct.repository.BoardRepository;
import hj.board.ct.repository.LikesRepository;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.util.SessionConst;
import hj.board.ct.web.member.login.LoginMemberFormInSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikesService {

    private final LikesRepository likesRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * /board/list 요청 시 Likes의 처음 이미지 결정한다 - test 코드 만들기
     */
    public int determineInitImage(Long boardNum, HttpSession session) {

        LoginMemberFormInSession loginMemberFormInSession
                = (LoginMemberFormInSession)session.getAttribute(SessionConst.LOGIN_MEMBER);

        // board의 likes memberId를 가져와서
        Board board = boardRepository.findByNum(boardNum).orElseThrow(() -> {
            throw new IllegalStateException("존재하지 않는 게시글입니다.");
        });
        // session의 member id와 같은지 확인한다.
        Likes likes = board.getLikesList()
                .stream()
                .filter((like) ->
                        like.getMember().getMemberId()
                                .equals(loginMemberFormInSession.getMemberId()))
                .findFirst()
                .orElse(null);
        // 같으면 1을 반환하고 아니면 0을 반환한다
        if(likes != null) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * likes 저장하기 - test 코드 만들기
     */
    @Transactional
    public void saveLikes(Long boardNum, String memberId) {

        // board 불러오기
        Board findBoard = boardRepository.findByNum(boardNum).orElseThrow(
                () -> {
                    throw new IllegalStateException("존재하지 않는 게시글입니다.");
                });
        // memberId 불러오기
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> {
                    throw new IllegalStateException("존재하지 않는 사용자입니다.");
                });

        // likes 만들기
        Likes likes = Likes.createLikes(findMember, findBoard);
        // 저장하기
        likesRepository.save(likes);
    }

    /**
     * likes 삭제하기 - test 코드 만들기
     */
    @Transactional
    public void deleteLikes(Long boardNum, String memberId) {

        // board 불러오기
        Board findBoard = boardRepository.findByNum(boardNum).orElseThrow(
                () -> {
                    throw new IllegalStateException("존재하지 않는 게시글입니다.");
                });

        // findBoard에서 Likes를 가져와서
        // memberId가 같은 게 있는지 조사한다
        Likes findLikes = findBoard.getLikesList()
                .stream()
                .filter((likes) ->
                    likes.getMember().getMemberId().equals(memberId)
                )
                .findAny()
                .orElse(null);

        // 있으면 삭제한다.
        if(findLikes != null) {

            // Likes에서 삭제
//            likesRepository.delete(findLikes); // delete가 안 된다
            likesRepository.delete(findLikes.getNum());
        }

    }


}
