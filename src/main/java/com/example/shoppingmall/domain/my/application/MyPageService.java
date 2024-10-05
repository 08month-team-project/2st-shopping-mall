package com.example.shoppingmall.domain.my.application;


import com.example.shoppingmall.domain.my.dto.MyPageRequest;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.domain.user.type.UserRole;
import com.example.shoppingmall.domain.user.type.UserStatus;
import com.example.shoppingmall.global.exception.ErrorCode;
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
                UserStatus.ACTIVE,null); //? default
        //소개글 추가?
        /* 프론트에서 default 처리?
           따로 db에 저장되지않는 default image를 프론트에서 관리하고
           프론트에서는 백에서 null이 보내지면 기본이미지으로 설정되게 설정

           백에서 default 처리
            db에 image url을 default 처리
            default image 마저 프론트로 전송
         */
        userRepository.save(user);


        User user1=userRepository.findByEmail("example@gmail.com")
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));
        return new MyPageRequest(user);
    }
}
// 변동사항이 발생되면 이미지 url을 db에 저장 이미지는 s3에 저장

