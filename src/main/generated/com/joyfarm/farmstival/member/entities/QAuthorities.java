package com.joyfarm.farmstival.member.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthorities is a Querydsl query type for Authorities
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthorities extends EntityPathBase<Authorities> {

    private static final long serialVersionUID = 596642266L;

    public static final QAuthorities authorities = new QAuthorities("authorities");

    public final EnumPath<com.joyfarm.farmstival.member.constants.Authority> authority = createEnum("authority", com.joyfarm.farmstival.member.constants.Authority.class);

    public final SimplePath<Member> member = createSimple("member", Member.class);

    public QAuthorities(String variable) {
        super(Authorities.class, forVariable(variable));
    }

    public QAuthorities(Path<? extends Authorities> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthorities(PathMetadata metadata) {
        super(Authorities.class, metadata);
    }

}

