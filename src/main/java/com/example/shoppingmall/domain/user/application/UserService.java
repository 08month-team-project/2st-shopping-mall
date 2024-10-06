package com.example.shoppingmall.domain.user.application;

import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.dto.CheckEmailRequest;
import com.example.shoppingmall.domain.user.dto.SignupRequest;
import com.example.shoppingmall.domain.user.dto.SignupResponse;
import com.example.shoppingmall.domain.user.dto.UserResponse;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.domain.user.type.UserStatus;
import com.example.shoppingmall.global.exception.ErrorCode;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import com.example.shoppingmall.global.security.util.RedisAuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisAuthUtil redisAuthUtil;

    @Transactional
    public SignupResponse createUser(SignupRequest signupRequest){
        Optional<User> findUser = userRepository.findByEmail(signupRequest.getEmail());

        if (findUser.isPresent()){
            if (findUser.get().getStatus().equals(UserStatus.WITHDRAWAL)){
                userRepository.delete(findUser.get());
                userRepository.flush();
            }else {
                throw new UserException(ErrorCode.ALREADY_EXIST_USER);
            }
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
        Optional<User> findUser = userRepository.findByEmail(checkEmailRequest.getEmail());

        if (findUser.isPresent() && findUser.get().getStatus().equals(UserStatus.ACTIVE)){
            throw new UserException(ErrorCode.ALREADY_EXIST_USER);
        }

        return UserResponse.builder()
                .message("사용 가능한 이메일입니다.").build();
    }

    @Transactional
    public UserResponse inactiveUser(CustomUserDetails userDetails){
        Optional<User> user = userRepository.findById(userDetails.getUserId());
        if (user.isEmpty()){
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
        if (user.get().getStatus().equals(UserStatus.WITHDRAWAL)){
            throw new UserException(ErrorCode.ALREADY_DELETED_USER);
        }
        user.get().deleteUser();
        redisAuthUtil.deleteRefreshToken(user.get().getEmail());

        return UserResponse.builder()
                .message("정삭적으로 탈퇴되었습니다.").build();
    }

}
