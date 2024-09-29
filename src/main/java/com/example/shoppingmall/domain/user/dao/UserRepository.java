package com.example.shoppingmall.domain.user.dao;

import com.example.shoppingmall.domain.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
}
