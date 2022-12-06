package hj.board.ct.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPhone is a Querydsl query type for Phone
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPhone extends BeanPath<Phone> {

    private static final long serialVersionUID = 1728664333L;

    public static final QPhone phone = new QPhone("phone");

    public final StringPath phone1 = createString("phone1");

    public final StringPath phone2 = createString("phone2");

    public QPhone(String variable) {
        super(Phone.class, forVariable(variable));
    }

    public QPhone(Path<? extends Phone> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPhone(PathMetadata metadata) {
        super(Phone.class, metadata);
    }

}

