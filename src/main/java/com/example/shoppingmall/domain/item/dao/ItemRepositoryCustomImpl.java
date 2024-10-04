package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.dto.ItemResponse;
import com.example.shoppingmall.domain.item.type.SortCondition;
import com.example.shoppingmall.domain.item.type.StatusCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.shoppingmall.domain.item.domain.QCategory.category;
import static com.example.shoppingmall.domain.item.domain.QCategoryItem.categoryItem;
import static com.example.shoppingmall.domain.item.domain.QItem.item;
import static com.example.shoppingmall.domain.item.type.ItemStatus.IN_STOCK;
import static com.example.shoppingmall.domain.item.type.SortCondition.*;
import static com.example.shoppingmall.domain.item.type.StatusCondition.AVAILABLE;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
@Repository
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory factory;


    private final int SEARCH_PAGE_SIZE = 12;

    @Override
    public Page<ItemResponse> searchItems(Long categoryId,
                                          String itemName,
                                          StatusCondition statusCondition,
                                          SortCondition sortCondition,
                                          Integer pageNumber) {

        PageRequest request = getPageRequest(pageNumber, sortCondition);


        return inCategorySearch(categoryId, itemName, statusCondition, sortCondition, request);

    }


    private Page<ItemResponse> inCategorySearch(Long categoryId,
                                                String itemName,
                                                StatusCondition statusCondition,
                                                SortCondition sortCondition,
                                                Pageable pageable) {

        List<ItemResponse> resultContents = factory.select(
                        Projections.bean(ItemResponse.class,
                                item.id.as("itemId"),
                                item.name.as("itemName"),
                                item.price,
                                item.thumbnailUrl,
                                item.hitCount.as("hits"),
                                categoryItem.category.id.as("categoryId"),
                                categoryItem.category.categoryName,
                                item.expiredAt,
                                item.createdAt,
                                item.status
                        ))
                .from(categoryItem)
                .join(item).on(categoryItem.item.id.eq(item.id))
                .join(category).on(categoryItem.category.id.eq(category.id))
                .where(
                        categoryCondition(categoryId),
                        itemNameCondition(itemName),
                        filterCondition(statusCondition, sortCondition)
                )
                .orderBy(sortCondition(sortCondition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = factory.select(categoryItem.count())
                .from(categoryItem)
                .join(item).on(categoryItem.item.id.eq(item.id))
                .join(category).on(categoryItem.category.id.eq(category.id))
                .where(
                        categoryCondition(categoryId),
                        itemNameCondition(itemName),
                        filterCondition(statusCondition, sortCondition)
                )
                .orderBy(sortCondition(sortCondition));

        return PageableExecutionUtils.getPage(resultContents, pageable,
                totalCount::fetchOne);
    }

    private BooleanExpression categoryCondition(Long categoryId) {

        if (categoryId == null) {
            return null;
        }
        return category.id.eq(categoryId);
    }


    private BooleanExpression itemNameCondition(String itemName) {

        if (!StringUtils.hasText(itemName)) {
            return null;
        }
        return item.name.contains(itemName);
    }


    /**
     * 여기서 분명 많은 일이 있었는데, 기억이.. 안난다..
     */
    private BooleanExpression filterCondition(StatusCondition status,
                                              SortCondition sort) {

        // 구매 가능한 것들만 조회
        // 정렬에서 마감임박순 이라는 키워드 선택이 전체조회로 잡혀도, 구매가능한 것 만 보이게 하였음
        if (sort != null && sort.equals(DEADLINE) || status != null && status.equals(AVAILABLE)) {

            return item.status.eq(IN_STOCK)
                    .and(item.expiredAt.after(LocalDateTime.now()));
        }

        return null;
    }


    private OrderSpecifier<?> sortCondition(SortCondition sort) {

        if (sort == null || sort.equals(LATEST)) {
            return item.createdAt.desc();
        }

        if (sort.equals(PRICE)) {
            return item.price.asc();
        }

        if (sort.equals(HITS)) {
            return item.hitCount.desc();
        }

        if (sort.equals(DEADLINE)) {
            return item.expiredAt.asc();
        }

        // 기본 설정은 최신순
        return item.createdAt.desc();
    }

    private PageRequest getPageRequest(Integer pageNumber, SortCondition sortCondition) {

        // 프론트에서는 페이지를 1부터 시작하고, 백엔드에서는 Page 의 첫페이지가 0 이기때문
        if (pageNumber == null || pageNumber == 0) {
            pageNumber = 0;
        } else {
            pageNumber--;
        }

        if (sortCondition == null || sortCondition.equals(LATEST)) {
            return PageRequest.of(pageNumber, SEARCH_PAGE_SIZE, DESC, "created_at");
        }

        if (sortCondition.equals(PRICE)) {
            return PageRequest.of(pageNumber, SEARCH_PAGE_SIZE, ASC, "item_price");
        }

        if (sortCondition.equals(HITS)) {
            return PageRequest.of(pageNumber, SEARCH_PAGE_SIZE, DESC, "hit_count");
        }

        if (sortCondition.equals(DEADLINE)) {
            return PageRequest.of(pageNumber, SEARCH_PAGE_SIZE, ASC, "expiredAt");
        }

        // 기본 설정은 최신순
        return PageRequest.of(pageNumber, SEARCH_PAGE_SIZE, DESC, "created_at");
    }

}
