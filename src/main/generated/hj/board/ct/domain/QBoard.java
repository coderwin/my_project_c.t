package hj.board.ct.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 1715930245L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final StringPath boardContent = createString("boardContent");

    public final NumberPath<Long> boardHits = createNumber("boardHits", Long.class);

    public final StringPath boardRock = createString("boardRock");

    public final StringPath boardTitle = createString("boardTitle");

    public final DateTimePath<java.time.LocalDateTime> boardUpdatedate = createDateTime("boardUpdatedate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> boardWritingdate = createDateTime("boardWritingdate", java.time.LocalDateTime.class);

    public final ListPath<Likes, QLikes> likesList = this.<Likes, QLikes>createList("likesList", Likes.class, QLikes.class, PathInits.DIRECT2);

    public final QMember member;

    public final NumberPath<Long> num = createNumber("num", Long.class);

    public final ListPath<Reply, QReply> replyList = this.<Reply, QReply>createList("replyList", Reply.class, QReply.class, PathInits.DIRECT2);

    public final ListPath<UploadFile, QUploadFile> uploadFileList = this.<UploadFile, QUploadFile>createList("uploadFileList", UploadFile.class, QUploadFile.class, PathInits.DIRECT2);

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

