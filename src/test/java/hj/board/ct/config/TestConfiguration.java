package hj.board.ct.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hj.board.ct.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @PersistenceContext
    private EntityManager em;

    @Bean
    @Profile("test")
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    @Profile("test")
    public MemberRepository memberRepository() {
        return new MemberRepository(this.em, jpaQueryFactory());
    }
}
