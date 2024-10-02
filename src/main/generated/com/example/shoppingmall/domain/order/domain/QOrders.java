package com.example.shoppingmall.domain.order.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrders is a Querydsl query type for Orders
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrders extends EntityPathBase<Orders> {

    private static final long serialVersionUID = -1785710746L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrders orders = new QOrders("orders");

    public final com.example.shoppingmall.domain.common.QBaseTimeEntity _super = new com.example.shoppingmall.domain.common.QBaseTimeEntity(this);

    public final com.example.shoppingmall.domain.user.domain.QAddress address;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<com.example.shoppingmall.domain.order.type.OrderStatus> status = createEnum("status", com.example.shoppingmall.domain.order.type.OrderStatus.class);

    public final com.example.shoppingmall.domain.user.domain.QUser user;

    public QOrders(String variable) {
        this(Orders.class, forVariable(variable), INITS);
    }

    public QOrders(Path<? extends Orders> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrders(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrders(PathMetadata metadata, PathInits inits) {
        this(Orders.class, metadata, inits);
    }

    public QOrders(Class<? extends Orders> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.example.shoppingmall.domain.user.domain.QAddress(forProperty("address")) : null;
        this.user = inits.isInitialized("user") ? new com.example.shoppingmall.domain.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

