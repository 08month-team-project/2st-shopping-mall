package com.example.shoppingmall.domain.user.application;

import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.dto.CheckEmailRequest;
import com.example.shoppingmall.domain.user.dto.SignupRequest;
import com.example.shoppingmall.domain.user.dto.SignupResponse;
import com.example.shoppingmall.domain.user.dto.UserResponse;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public SignupResponse createUser(SignupRequest signupRequest){

        if (userRepository.existsByEmail(signupRequest.getEmail())){
            throw new UserException(ErrorCode.ALREADY_EXIST_USER);
        }
        String encodedPwd = bCryptPasswordEncoder.encode(signupRequest.getPassword());
        User user = signupRequest.dtoToEntity(encodedPwd);
        User savedUser = userRepository.save(user);

        if (savedUser.getId() != null){
            return SignupResponse.builder()
                    .message("success signup").build();
        }else {
            throw new UserException(ErrorCode.CREATE_USER_FAILED);
        }

    }

    @Transactional
    public UserResponse checkEmailDuplicate(CheckEmailRequest checkEmailRequest){
        if (userRepository.existsByEmail(checkEmailRequest.getEmail())){
            throw new UserException(ErrorCode.ALREADY_EXIST_USER);
        }
        return UserResponse.builder()
                .message("사용 가능한 이메일입니다.").build();
    }

}
