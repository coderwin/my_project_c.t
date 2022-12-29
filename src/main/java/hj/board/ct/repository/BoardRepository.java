package hj.board.ct.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hj.board.ct.domain.Board;
import hj.board.ct.util.LocalDateParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static hj.board.ct.domain.QBoard.board;
import static hj.board.ct.domain.QMember.member;


@Repository
@Slf4j
@RequiredArgsConstructor
public class BoardRepository {

    @PersistenceContext
    private final EntityManager em; // jpa 사용
    private final JPAQueryFactory query; // querydsl method 사용

    /**
     * 게시글 저장하기
     */
    public void save(Board board) {
        em.persist(board);
    }

    /**
     * 게시글 불러오기 by board_num
     */
    public Optional<Board> findByNum(Integer num) {
        
       return Optional.ofNullable(em.find(Board.class, num));
    }

    /**
     * 게시글 모두 가져오기 or 조건에 맞춰서(by 아이디, 제목, 내용, 날짜)
     * -- 최신 게시글 순으로 나온다
     * param - startOfLimit : offset의 start 부분, showedBoards : limit의 end 부분
     */
    public List<Board> findAll(BoardSearchCond boardSearchCond,
                               int startOfLimit,
                               int showedBoards ) {
        
        // querydsl 사용하기
        return query
                .select(board)
                .from(board)
                .where(likeBoardId(boardSearchCond.getId()),
                        likeBoardTitle(boardSearchCond.getTitle()),
                        likeBoardContent(boardSearchCond.getContent()),
                        betweenBoardWritingdate(boardSearchCond.getWritingdate()))
                .orderBy(board.num.desc())
                .offset(startOfLimit)
                .limit(showedBoards)
                .fetch();
    }

    /**
     * 게시글 모두 가져오기 or 조건에 맞춰서(by 아이디, 제목, 내용, 날짜)
     */
    public List<Board> findAll(BoardSearchCond boardSearchCond) {

        // querydsl 사용하기
        return query
                .select(board)
                .from(board)
                .where(likeBoardId(boardSearchCond.getId()),
                        likeBoardTitle(boardSearchCond.getTitle()),
                        likeBoardContent(boardSearchCond.getContent()),
                        betweenBoardWritingdate(boardSearchCond.getWritingdate()))
                .fetch();
    }

    /**
     * 게시글 삭제하기
     */
    public void delete(Board board) {
        em.remove(board);
    }

    /**
     * 회원정보를 클릭 했을 때, 자기가 쓴 게시글 보이게 하기 _ by id
     */
    public List<Board> findAllById(String id) {
        // querydsl 사용
        return query.select(board)
                .from(board)
                .where(board.member.memberId.eq(id))
                .fetch();
    }

    /**
     * 회원정보를 클릭 했을 때, 자기가 쓴 게시글 보이게 하기 _ using join
     *             Q.board에서는 board내의 member를 사용하면 query가 이상해지는 건가?(2022.10.22)
     */
    public List<Board> findAllByMember(String id) {
       return query
               .select(board)
               .from(board)
               .join(board.member, member)
               .on(member.memberId.eq(id))
               .fetch();
    }

    // == helping main method ==//
    ///// findAll의 where에 들어가는 조건을 정리한다. /////
    private BooleanExpression betweenBoardWritingdate(String writingdate) {

        if(StringUtils.hasText(writingdate)) {
            // String을 LocalDateTime으로 바꾸면서 시작시간과 끝시간을 만들기
            LocalDateParser localDateParser = new LocalDateParser(writingdate);

            return board.boardWritingdate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;
    }

    // 내용 검색
    private BooleanExpression likeBoardContent(String content) {
        if(StringUtils.hasText(content)) {
            return board.boardContent.like("%" + content + "%");
        }
        return null;
    }

    // 제목 검색
    private BooleanExpression likeBoardTitle(String title) {
        if(StringUtils.hasText(title)) {
            return board.boardTitle.like("%" + title + "%");
        }
        return null;
    }

    // 아이디 검색
    private BooleanExpression likeBoardId(String id) {
        if(StringUtils.hasText(id)) {
            return board.member.memberId.like("%" + id + "%");
        }
        return null;
    }










}
