package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    /**
     * 물품 상세보기 api 요청 시 사용되지도 않는 cart 에 대한 추가 쿼리가 실행되는 문제 발생
     * 로직도 살펴보고, user 와 cart 양쪽에서 Lazy 설정을 해주었음에도 추가 쿼리가 실행되었습니다.
     * 찾아보니 @OneToOne 양방향 연관 관계에서 연관 관계의 주인이 아닌 쪽 엔티티를 조회할 때, Lazy로 동작할 수 없다고 합니다.
     * 설계 자체를 바꾸기엔 수정사항이 너무 많고, 일정이 얼마 남지 않았으니 임시방편으로 cart 까지 패치조인하는 방법을 취했습니다.
     */
    @Query("select i from Item i " +
            " join fetch i.user u" +
            " join fetch u.cart "+
            " join fetch i.stocks is " +
            " join fetch is.clothingSize " +
            " where i.id = :itemId")
    Optional<Item> findItemAndStockAndSeller(@Param("itemId") Long itemId);
}
