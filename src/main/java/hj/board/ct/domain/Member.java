package hj.board.ct.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter @Setter
//@ToString
public class Member {

    // 회원 정보
    @Id @GeneratedValue
    @Column(name = "member_num")
    private Long memberNum; // 회원가입 번호/순서
    @Column(length = 30) @NotNull
    private String memberName; // 회원 이름

    @Column(name = "member_id", unique = true, length = 30) @NotNull
    private String memberId; // 회원 아이디
    @Column(length = 21) @NotNull
    private String memberPwd; // 회원 비밀번호
    @Column(length = 50) @NotNull
    private String memberEmail; // 회원 이메일

    @Embedded
    private Phone memberPhone; // 회원 전화번호 2개 이상

    @NotNull
    private LocalDate memberBirthday; // 회원 생일
    @NotNull
    private LocalDateTime memberJoindate; //회원가입 날짜
    @NotNull
    private LocalDateTime memberUpdatedate; //회원정보 수정 날짜

    // 회원가입 동의 체크
    @Enumerated(EnumType.STRING)
    private MemberChecking individualInformationAgreement; // 개인정보 사용 동의
    @Enumerated(EnumType.STRING)
    private MemberChecking memberJoinAgreement; // 회원가입 동의

    // 회원의 가입현황 체크
    @Enumerated(EnumType.STRING)
    private SubscriptionStatusOfMember subscriptionStatus; // 가입되었나, 탈퇴했나
    private LocalDateTime memberWithdrawaldate; // 회원 탈퇴 날짜

//    // 회원이 작성한 모든 게시글 가져오기
//    // persist한 것은 아이디를 삭제하더라도 게시물은 남아있게 하기위해, 아이디 바뀌면 아이디 바꿔주기
    @OneToMany(mappedBy = "member")
    private List<Board> boardList; // 회원이 쓴 게시글 가져오기

    // likes 삭제에 사용
    @OneToMany(mappedBy = "member")
    private List<Likes> likesList;

    // == 생성 메서드 == //

    public static Member createMember(String id,
                               String pwd,
                               String name,
                               String email,
                               String birthday,
                               MemberChecking informationAgreement,
                               MemberChecking joinAgreement,
                               String phone1,
                               String phone2
                               ) {

        Member member = new Member();

        Phone phone = new Phone(phone1, phone2);
        LocalDate myBirthday = member.changeTypeForBirthday(birthday);

        // 회원 정보 입력하기
        member.setMemberId(id);
        member.setMemberPwd(pwd);
        member.setMemberName(name);
        member.setMemberEmail(email);

        member.setMemberPhone(phone);
        member.setMemberBirthday(myBirthday);

        member.setIndividualInformationAgreement(informationAgreement);
        member.setMemberJoinAgreement(joinAgreement);

        member.setMemberJoindate(LocalDateTime.now());
        member.setMemberUpdatedate(LocalDateTime.now());

        // 회원가입 현황 - J(join)
        member.setSubscriptionStatus(SubscriptionStatusOfMember.J);

        return member;
    }

    // String을 LocalDate로 바꾸기
    public LocalDate changeTypeForBirthday(String memberBirthday) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss.SSS");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(memberBirthday, formatter);
        return date;
    }

    // String을 LocalDate로 바꾸기 - 임시로 만들어 두기 - 나중에 삭제하기
    public LocalDate changeTypeForBirthdayTemporarily(String memberBirthday) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss.SSS");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(memberBirthday, formatter);
        return date;
    }



}
