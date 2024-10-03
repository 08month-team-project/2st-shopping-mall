package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.type.SortCondition;
import com.example.shoppingmall.domain.item.type.StatusCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.shoppingmall.domain.item.domain.QCategoryItem.categoryItem;
import static com.example.shoppingmall.domain.item.domain.QItem.item;
import static com.example.shoppingmall.domain.item.type.ItemStatus.IN_STOCK;
import static com.example.shoppingmall.domain.item.type.SortCondition.*;
import static com.example.shoppingmall.domain.item.type.StatusCondition.AVAILABLE;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory factory;

    private final int SEARCH_PAGE_SIZE = 12;

    @Override
    public Page<Item> searchItems(Long categoryId,
                                  String itemName,
                                  StatusCondition statusCondition,
                                  SortCondition sortCondition,
                                  int pageNumber) {

        PageRequest request = getPageRequest(pageNumber, sortCondition);

        if (categoryId == null) {
            return noCategorySearch(itemName, statusCondition, sortCondition, request);
        }
        return inCategorySearch(categoryId, itemName, statusCondition, sortCondition, request);

    }

    private PageRequest getPageRequest(int pageNumber, SortCondition sortCondition) {

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

    private Page<Item> inCategorySearch(Long categoryId,
                                        String itemName,
                                        StatusCondition statusCondition,
                                        SortCondition sortCondition,
                                        Pageable pageable) {

        List<Item> resultContents = factory.selectFrom(item)
                .join(item.categoryItems, categoryItem)
                .where(
                        categoryCondition(categoryId),
                        itemNameCondition(itemName),
                        filterCondition(statusCondition, sortCondition)
                )
                .orderBy(sortCondition(sortCondition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = factory.select(item.count())
                .join(item.categoryItems, categoryItem)
                .where(
                        categoryCondition(categoryId),
                        itemNameCondition(itemName),
                        filterCondition(statusCondition, sortCondition)
                )
                .orderBy(sortCondition(sortCondition));

        return PageableExecutionUtils.getPage(resultContents, pageable,
                totalCount::fetchOne);
    }

    private Page<Item> noCategorySearch(String itemName,
                                        StatusCondition statusCondition,
                                        SortCondition sortCondition,
                                        Pageable pageable) {

        List<Item> resultContents = factory.selectFrom(item)
                .where(
                        itemNameCondition(itemName),
                        filterCondition(statusCondition, sortCondition)
                )
                .orderBy(sortCondition(sortCondition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = factory.select(item.count())
                .where(
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
        return categoryItem.category.id.eq(categoryId);
    }

    private BooleanExpression itemNameCondition(String itemName) {

        if (!StringUtils.hasText(itemName)) {
            return null;
        }

        return item.name.contains(itemName);
    }


    private BooleanExpression filterCondition(StatusCondition status,
                                              SortCondition sort) {

        // 구매 가능한 것들만 조회
        // 정렬에서 마감임박순 이라는 키워드 선택 시 전체조회로 잡혀도, 구매가능한 것 만 보이게 하였음
        if (sort.equals(DEADLINE) || status.equals(AVAILABLE)) {
            return item.status.eq(IN_STOCK)
                    .and(item.expiredAt.after(LocalDateTime.now()));
        }

        // 전체 조회
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
}
