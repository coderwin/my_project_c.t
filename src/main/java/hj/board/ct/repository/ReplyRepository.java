package hj.board.ct.repository;

import hj.board.ct.domain.Likes;
import hj.board.ct.domain.Reply;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ReplyRepository {

    @PersistenceContext
    private final EntityManager em;

    // 저장하기
    public void save(Reply reply) {

        em.persist(reply);
    }

    // reply 가져오기 by num
    public Reply findByNum(Long num) {
        return em.find(Reply.class, num);
    }

    // 삭제하기
    public void delete(Reply reply) {
        em.remove(reply);
    }






}
