package com.example.shoppingmall.domain.user.dao;

import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.domain.user.type.UserRole;
import com.example.shoppingmall.domain.user.type.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자정보를 데이터베이스에 저장합니다")
    void saveUser(){
        //given
        Address address = Address.builder()
                .street("강남대로 123")
                .city("서울 특별시 강남구")
                .zipcode("1101").build();
        User user = User.builder()
                .email("example@gmail.com")
                .name("홍길동")
                .nickname("길동이")
                .password("mypass1234")
                .gender(Gender.MALE)
                .phoneNumber("010-1234-1234")
                .address(address).build();

        //when
        User savedUser = userRepository.save(user);

        //then
        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(UserRole.CUSTOMER,savedUser.getRole());
        assertEquals(UserStatus.ACTIVE,savedUser.getStatus());
    }

    @Test
    @DisplayName("이메일 정보를 가지고 데이터베이스에서 중복된 사용자를 찾습니다.")
    void findByEmail() {
        //given
        Address address = Address.builder()
                .street("강남대로 123")
                .city("서울 특별시 강남구")
                .zipcode("1101").build();
        User user = User.builder()
                .email("example@gmail.com")
                .name("홍길동")
                .nickname("길동이")
                .password("mypass1234")
                .gender(Gender.MALE)
                .phoneNumber("010-1234-1234")
                .address(address).build();
        String email = "example@gmail.com";

        //when
        User savedUser = userRepository.save(user);
        boolean isExist = userRepository.existsByEmail(email);

        //then
        assertTrue(isExist);
        assertEquals(user.getEmail(),savedUser.getEmail());
        assertEquals(user.getName(),savedUser.getName());
    }

}