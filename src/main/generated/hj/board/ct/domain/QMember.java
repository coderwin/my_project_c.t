package hj.board.ct.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1960257755L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final ListPath<Board, QBoard> boardList = this.<Board, QBoard>createList("boardList", Board.class, QBoard.class, PathInits.DIRECT2);

    public final EnumPath<MemberChecking> individualInformationAgreement = createEnum("individualInformationAgreement", MemberChecking.class);

    public final ListPath<Likes, QLikes> likesList = this.<Likes, QLikes>createList("likesList", Likes.class, QLikes.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> memberBirthday = createDate("memberBirthday", java.time.LocalDate.class);

    public final StringPath memberEmail = createString("memberEmail");

    public final StringPath memberId = createString("memberId");

    public final EnumPath<MemberChecking> memberJoinAgreement = createEnum("memberJoinAgreement", MemberChecking.class);

    public final DateTimePath<java.time.LocalDateTime> memberJoindate = createDateTime("memberJoindate", java.time.LocalDateTime.class);

    public final StringPath memberName = createString("memberName");

    public final NumberPath<Long> memberNum = createNumber("memberNum", Long.class);

    public final QPhone memberPhone;

    public final StringPath memberPwd = createString("memberPwd");

    public final DateTimePath<java.time.LocalDateTime> memberUpdatedate = createDateTime("memberUpdatedate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> memberWithdrawaldate = createDateTime("memberWithdrawaldate", java.time.LocalDateTime.class);

    public final EnumPath<SubscriptionStatusOfMember> subscriptionStatus = createEnum("subscriptionStatus", SubscriptionStatusOfMember.class);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberPhone = inits.isInitialized("memberPhone") ? new QPhone(forProperty("memberPhone")) : null;
    }

}

