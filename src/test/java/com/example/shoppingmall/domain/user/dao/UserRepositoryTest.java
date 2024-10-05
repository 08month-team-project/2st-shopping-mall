package com.example.shoppingmall.domain.user.dao;

import com.example.shoppingmall.TestQueryDslConfig;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.domain.user.type.UserRole;
import com.example.shoppingmall.domain.user.type.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestQueryDslConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;
    @BeforeEach
    public void init(){
        Address address = Address.builder()
                .city("서울 특별시 강남구 강남대로 123")
                .zipcode("1101").build();
        user = User.builder()
                .email("example@gmail.com")
                .name("홍길동")
                .nickname("길동이")
                .password("mypass1234")
                .gender(Gender.MALE)
                .phoneNumber("010-1234-1234")
                .address(address).build();
    }

    @Test
    @DisplayName("사용자정보를 데이터베이스에 저장합니다")
    void saveUser(){

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