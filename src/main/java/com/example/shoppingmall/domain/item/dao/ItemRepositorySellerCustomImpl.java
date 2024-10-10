package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.dto.SellerItemResponse;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.shoppingmall.domain.item.domain.QCategory.category;
import static com.example.shoppingmall.domain.item.domain.QClothingSize.clothingSize;
import static com.example.shoppingmall.domain.item.domain.QItem.item;
import static com.example.shoppingmall.domain.item.domain.QItemStock.itemStock;
import static com.example.shoppingmall.domain.item.domain.QCategoryItem.categoryItem; // 추가
import static com.example.shoppingmall.domain.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class ItemRepositorySellerCustomImpl implements ItemRepositorySellerCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SellerItemResponse> findAllByUserAndStatus(Long userId, ItemStatus status, Pageable pageable) {

        // 페이징을 위한 쿼리 실행 (offset, limit 사용)
        List<SellerItemResponse> content = queryFactory
                .select(Projections.constructor(SellerItemResponse.class,
                        item.id,
                        user.nickname,
                        item.name,
                        item.thumbnailUrl,
                        item.description,
                        item.price,
                        itemStock.stock,
                        clothingSize.sizeName,
                        category.categoryName,
                        item.status,
                        item.expiredAt))
                .from(item)
                .join(item.user, user)
                .join(item.stocks, itemStock) // OneToMany 관계에서 join 사용
                .join(item.categoryItems, categoryItem) // category_item과의 조인
                .join(categoryItem.category, category) // category와의 조인
                .join(itemStock.clothingSize, clothingSize) // clothing_size와의 조인
                .where(
                        user.id.eq(userId),
                        itemStateCondition(status)
                )
                .orderBy(item.expiredAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(item.count())
                .from(item)
                .join(item.user, user)
                .join(item.stocks, itemStock)
                .join(item.categoryItems, categoryItem) // category_item과의 조인
                .where(
                        user.id.eq(userId),
                        itemStateCondition(status)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression itemStateCondition(ItemStatus status) {
        LocalDateTime now = LocalDateTime.now();

        return switch (status) {
            case IN_STOCK ->
                    item.expiredAt.gt(now).and(itemStock.stock.gt(0));
            case ALL_OUT_OF_STOCK ->
                    item.expiredAt.lt(now).or(itemStock.stock.eq(0));
            default ->
                    null;
        };
    }
}
