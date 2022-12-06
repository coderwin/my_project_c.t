package hj.board.ct.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hj.board.ct.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static hj.board.ct.domain.QUploadFile.uploadFile;

@Repository
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UploadFileRepository {

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory query;

    /**
     * 파일 이름 저장하기
     */
    @Transactional
    public void save(UploadFile uploadFile) {
        em.persist(uploadFile);
    }

    /**
     * 파일 이름 불러오기
     */
    public UploadFile findByNum(Long num) {
        return em.find(UploadFile.class, num);
    }

    /**
     * 파일 이름 불러오기
     * - test에 사용
     */
    public List<UploadFile> findAllByBoardNum(Long boardNum) {
        return query
                .select(uploadFile)
                .from(uploadFile)
                .where(uploadFile.board.num.eq(boardNum))
                .fetch();
    }

    /**
     * 파일 이름 삭제하기 by jpa
     */
    @Transactional
    public void delete(UploadFile uploadFile) {
        em.remove(uploadFile);
    }

    /**
     * 첨부파일 삭제하기 by boardNum
     */
    @Transactional
    public void deleteByBoardNum(Long num) {

        query
            .delete(uploadFile)
            .where(uploadFile.board.num.eq(num))
            .execute();



    }
}
