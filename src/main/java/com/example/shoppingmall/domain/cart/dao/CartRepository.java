package com.example.shoppingmall.domain.cart.dao;

import com.example.shoppingmall.domain.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);

    @Query("select c.id from Cart c where c.user.id = :userId")
    Long findCartId(@Param("userId") Long userId);
}
