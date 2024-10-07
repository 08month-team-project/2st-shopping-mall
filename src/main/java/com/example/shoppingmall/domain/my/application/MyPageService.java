package com.example.shoppingmall.domain.my.application;


import com.example.shoppingmall.domain.my.dto.MyPageRequest;
import com.example.shoppingmall.domain.my.dto.MyPageResponse;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.global.exception.ErrorCode;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    public MyPageResponse profileCheck(CustomUserDetails userDetails) {

        //소개글 추가?
        User user=userRepository.findById(userDetails.getUserId())
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));
        return new MyPageResponse(user);
    }

    @Transactional
    public void profileModify(CustomUserDetails userDetails, MyPageRequest myPageRequest) {
        User user=userRepository.findById(userDetails.getUserId())
                .orElseThrow(()->new UserException(ErrorCode.USER_NOT_FOUND));

        User modifyUser=User.builder()
                .email(myPageRequest.getEmail())
                .name(myPageRequest.getName())
                .nickname(myPageRequest.getNickname())
                .password(myPageRequest.getPassword())
                .gender(myPageRequest.getGender())
                .phoneNumber(myPageRequest.getPhone())
                .address(myPageRequest.getAddress())
                .build();
        /* 프론트에서 default 처리?
           따로 db에 저장되지않는 default image를 프론트에서 관리하고
           프론트에서는 백에서 null이 보내지면 기본이미지으로 설정되게 설정

           백에서 default 처리
            db에 image url을 default 처리
            default image 마저 프론트로 전송
         */
    }
}
// 변동사항이 발생되면 이미지 url을 db에 저장 이미지는 s3에 저장

