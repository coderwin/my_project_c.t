package hj.board.ct.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hj.board.ct.domain.Member;
import hj.board.ct.util.LocalDateParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static hj.board.ct.domain.QMember.member;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberRepository {

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory query; // querydsl method 사용

    /**
     * 회원가입 정보 저장 및 회원정보 수정 같이 한다.
     */
    public void save(Member member) {
        em.persist(member);
    }

    /**
     * member_num으로 회원 찾기
     */
    public Member findByNum(Long num) {
        return em.find(Member.class, num);
    }

    /**
     * member_id로 회원 찾기
     */
    public Optional<Member> findById(String id) {
       return query
               .select(member)
               .from(member)
               .where(member.memberId.eq(id))
               .fetch()
               .stream()
               .filter(m -> m.getMemberId().equals(id))// m == Member
               .findFirst();
    }

    /**
     * member_id로 회원 정보 삭제하기
     */
    public void delete(Member member) {

        em.remove(member);

//        String sql = "delete from Member m where m.memberId = :id";
//
//        em.createQuery(sql, Member.class)
//                .setParameter("id", id);
    }

    /*****************  운영자에게 필요한 기능 *************************

    /*
     * 모든 회원정보 가져오기( + 조건 입력하기)
     */
    public List<Member> findAll(MemberSearchCond cond) {

        String memberId = cond.getMemberId();
        String memberJoindate = cond.getMemberJoindate();
        LocalDate memberBirthday = cond.getMemberBirthday();

        List<Member> result = query
                .select(member)
                .from(member)
                .where(likeMemberId(memberId), BetweenMemberJoindate(memberJoindate), eqMemberBirthday(memberBirthday))
                .fetch();

        return result;
    }

    private BooleanExpression eqMemberBirthday(LocalDate memberBirthday) {
        if(memberBirthday != null) {
            return member.memberBirthday.eq(memberBirthday);
        }
        return null;
    }

    private BooleanExpression BetweenMemberJoindate(String memberJoindate) {
        if(StringUtils.hasText(memberJoindate)) {
            // 찾는 시간을 입력받기
            LocalDateParser localDateParser = new LocalDateParser(memberJoindate);

            return member.memberJoindate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;
    }

    private BooleanExpression likeMemberId(String memberId) {
        if(StringUtils.hasText(memberId)) {
            return member.memberId.like("%" + memberId + "%");
        }
        return null;
    }

    /*************로그인시 아이디 session 저장에 필요 ***********/
    /**
     * 로그인 시, 회원 찾기
     */
    public Optional<Member> findMemberByLoginId(String loginId) {
        return query
                .select(member)
                .from(member)
                .fetch()
                .stream()
                .filter((m) ->
            m.getMemberId().equals(loginId))
                .findFirst();
    }

    /*************  회원 아이디, 비밀번호 찾기  **************/

    /**
     * member_name and member_phone로 회원아이디 찾기
     */
    public List<String> findIdByNameAndPhone(String name, String phone) {
        return query
                .select(member.memberId)
                .from(member)
                .where(member.memberName.eq(name),
                        member.memberPhone.phone1.eq(phone))
                .fetch();
    }

    /**
     * member_name and member_email로 회원아이디 찾기
     */
    public List<String> findIdByNameAndEmail(String name, String email) {
        return query
                .select(member.memberId)
                .from(member)
                .where(member.memberName.eq(name),
                        member.memberEmail.eq(email))
                .fetch();
    }

    /**
     * member_id and member_phone로 회원 비밀번호 찾기
     */
    public List<String> findPwdByIdAndPhone(String id, String phone) {
        return query
                .select(member.memberPwd)
                .from(member)
                .where(member.memberId.eq(id),
                        member.memberPhone.phone1.eq(phone))
                .fetch();
    }



    /**
     * member_id and member_email로 회원 비밀번호 찾기
     */
    public List<String> findPwdByIdAndEmail(String id, String email) {
        return query
                .select(member.memberPwd)
                .from(member)
                .where(member.memberId.eq(id),
                        member.memberEmail.eq(email))
                .fetch();
    }

    // == 해당 날짜 검색 하기 == //











}
