package com.example.shoppingmall.domain.item.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -1337892725L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final com.example.shoppingmall.domain.common.QBaseTimeEntity _super = new com.example.shoppingmall.domain.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> hitCount = createNumber("hitCount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ItemImage, QItemImage> images = this.<ItemImage, QItemImage>createList("images", ItemImage.class, QItemImage.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final ListPath<ItemStock, QItemStock> stocks = this.<ItemStock, QItemStock>createList("stocks", ItemStock.class, QItemStock.class, PathInits.DIRECT2);

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final com.example.shoppingmall.domain.user.domain.QUser user;

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.shoppingmall.domain.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

