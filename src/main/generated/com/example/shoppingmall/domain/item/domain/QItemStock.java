package com.example.shoppingmall.domain.item.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemStock is a Querydsl query type for ItemStock
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemStock extends EntityPathBase<ItemStock> {

    private static final long serialVersionUID = 328484171L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemStock itemStock = new QItemStock("itemStock");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final EnumPath<com.example.shoppingmall.domain.item.type.ClothingSize> size = createEnum("size", com.example.shoppingmall.domain.item.type.ClothingSize.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public QItemStock(String variable) {
        this(ItemStock.class, forVariable(variable), INITS);
    }

    public QItemStock(Path<? extends ItemStock> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemStock(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemStock(PathMetadata metadata, PathInits inits) {
        this(ItemStock.class, metadata, inits);
    }

    public QItemStock(Class<? extends ItemStock> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
    }

}

