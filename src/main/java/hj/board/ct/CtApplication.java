package hj.board.ct;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hj.board.ct.repository.MemberRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootApplication
public class CtApplication {

	public static void main(String[] args) {
		SpringApplication.run(CtApplication.class, args);
	}

//	@PersistenceContext
//	private EntityManager em;
//
//	@Bean
//	@Profile("test")
//	public JPAQueryFactory jpaQueryFactory() {
//		return new JPAQueryFactory(em);
//	}
//
//	@Bean
//	@Profile("test")
//	public MemberRepository memberRepository() {
//		return new MemberRepository(this.em, jpaQueryFactory());
//	}

	// test위한 초기 데이터 생성
//	@Bean
//	@Profile("test")
	public TestDataInit testDataInit(MemberRepository memberRepository) {
		return new TestDataInit(memberRepository);
	}

}
