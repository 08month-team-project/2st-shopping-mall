package com.example.shoppingmall.domain.order.dao;

import com.example.shoppingmall.domain.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
}
