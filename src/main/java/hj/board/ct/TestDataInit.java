package hj.board.ct;

import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import hj.board.ct.domain.Phone;
import hj.board.ct.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberRepository memberRepository;

    /**
     * test용 초기 데이터 추가
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("initial dataes for test");

        Member member1 = Member.createMember("test1",
                "12345",
                "하하",
                "test@test",
                "19990202",
                MemberChecking.Y,
                MemberChecking.N,
                "010-1234",
                "051-1234");

        memberRepository.save(member1);

        Member member2 = Member.createMember("test12",
                "12345",
                "하하2",
                "test2@test",
                "20001212",
                MemberChecking.Y,
                MemberChecking.Y,
                "010-5623",
                "052-365");

        memberRepository.save(member2);

        Member member3 = Member.createMember("test123",
                "123asd!@#$",
                "하하",
                "test2@test.com",
                "20220101",
                MemberChecking.Y,
                MemberChecking.Y,
                "01012341234",
                "0511231234");

        memberRepository.save(member3);
    }


}
