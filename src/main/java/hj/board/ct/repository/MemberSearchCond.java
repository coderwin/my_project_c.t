package hj.board.ct.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MemberSearchCond {
    private final String memberId; // 회원 아이디
    private final LocalDate memberBirthday; // 회원 생일
    private final String memberJoindate; //회원가입 날짜
}
