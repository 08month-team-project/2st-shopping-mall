package com.example.shoppingmall.domain.my.application;


import com.example.shoppingmall.domain.my.dto.MyPageRequest;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.domain.user.type.UserRole;
import com.example.shoppingmall.domain.user.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    public MyPageRequest check(UserDetails userDetails) {
        //testcode입니다.
        User user = new User(1L,"exaple@gmail.com","테스트","테스트",
                "1234567!a","01000000000",new Address(), Gender.MALE, UserRole.CUSTOMER,
                UserStatus.ACTIVE,"123.jpg");
        userRepository.save(user);

        User user1=userRepository.findByEmail("example@gmail.com");
        MyPageRequest myPageRequest = new MyPageRequest(user);
        return myPageRequest;
    }
}
