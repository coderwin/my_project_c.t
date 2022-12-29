package hj.board.ct.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hj.board.ct.domain.Likes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static hj.board.ct.domain.QLikes.likes;

@Repository
@Slf4j
@RequiredArgsConstructor
public class LikesRepository {

    @PersistenceContext
    private final EntityManager em;

    private final JPAQueryFactory query;

    // 저장하기
    public void save(Likes likes) {

        em.persist(likes);
    }

    // test에 사용
    // Likes 가져오기 by num
    public Likes findByNum(Integer num) {
        return em.find(Likes.class, num);
    }



    // 삭제하기 by jpa
    public void delete(Likes likes) {

        log.info("likesRepository delete에 왔다!!!!!");
        em.remove(likes);
    }

    // 삭제하기 by jpql
    public void delete(Integer num) {

        log.info("likesRepository delete에 왔다!!!!!");
        // 쿼리문 만들기
        String sql = "delete from Likes l where l.num = :num";

        // 쿼리 실행하기
        em.createQuery(sql)
                .setParameter("num", num)
                .executeUpdate();
    }



}
