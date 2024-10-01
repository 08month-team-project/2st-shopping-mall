package com.example.shoppingmall.domain.user.application;

import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.dto.AddressRequest;
import com.example.shoppingmall.domain.user.dto.SignupRequest;
import com.example.shoppingmall.domain.user.dto.SignupResponse;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    private String existingEmail;
    private String correctEmail;
    private SignupRequest signupRequest;
    private User user;
    @BeforeEach
    public void init(){
        //given
        existingEmail = "example@gmail.com";
        correctEmail = "example@gmail.com";
        String correctPwd = "example1234";
        String correctPhone = "010-1234-1234";
        AddressRequest addressRequest = AddressRequest.builder()
                .street("강남대로 123")
                .city("서울 특별시 강남구")
                .zipcode("1010").build();
        Address address = Address.builder()
                .street("강남대로 123")
                .city("서울 특별시 강남구")
                .zipcode("1010")
                .build();
        signupRequest = SignupRequest.builder()
                .email(correctEmail)
                .name("홍길동")
                .nickname("길동이")
                .password(correctPwd)
                .gender(Gender.MALE)
                .phoneNumber(correctPhone)
                .address(addressRequest)
                .build();
        user = User.builder()
                .id(1L)
                .email(correctEmail)
                .name("홍길동")
                .nickname("길동이")
                .password(correctPwd)
                .gender(Gender.MALE)
                .phoneNumber(correctPhone)
                .address(address)
                .build();
    }

    @Test
    @DisplayName("사용자 정보를 가지고 사용자를 생성합니다 - 회원가입 성공")
    void createUserSuccess() {
        //when
        when(userRepository.save(any())).thenReturn(user);
        SignupResponse response = userService.createUser(signupRequest);

        //then
        assertThat(response.getMessage()).isEqualTo("success signup");
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void createUserFailByDuplicateEmail(){
        //when
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        //then
        assertThrows(UserException.class, () -> userService.createUser(signupRequest));

    }
    @Test
    @DisplayName("회원가입 실패 - Server Error (DB)")
    void createUserFailByServerError(){
        // when
        when(userRepository.existsByEmail(correctEmail)).thenReturn(false);
        when(userRepository.save(any())).thenThrow(new UserException(ErrorCode.CREATE_USER_FAILED));

        // then
        assertThrows(UserException.class, () -> userService.createUser(signupRequest));
    }



}