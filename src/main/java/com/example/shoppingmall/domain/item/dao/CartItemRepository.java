package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("select ci from CartItem ci " +
            " join fetch ci.itemStock " +
            " join fetch ci.item " +
            " where ci.cart.id = :cartId and ci.itemStock.id = :itemStockId")
    Optional<CartItem> findCartItemByFetch(@Param("cartId") long cartId, @Param("itemStockId") long itemStockId);


    @Query("select ci from CartItem ci " +
            " join fetch ci.cart " +
            " join fetch ci.item " +
            " join fetch ci.itemStock " +
            " where ci.id = :cartItemId")
    Optional<CartItem> findCartItemByFetch(@Param("cartItemId") long cartItemId);

}
