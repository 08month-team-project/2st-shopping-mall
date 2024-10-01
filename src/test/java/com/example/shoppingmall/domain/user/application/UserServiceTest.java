package com.example.shoppingmall.domain.user.application;

import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.dto.AddressRequest;
import com.example.shoppingmall.domain.user.dto.SignupRequest;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.global.exception.ErrorCode;
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

    @Test
    @DisplayName("사용자 정보를 가지고 사용자를 생성합니다 - 회원가입 성공")
    void createUserSuccess() {
        //Given
        String correctEmail = "example@gmail.com";
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
        SignupRequest signupRequest = SignupRequest.builder()
                .email(correctEmail)
                .name("홍길동")
                .nickname("길동이")
                .password(correctPwd)
                .gender(Gender.MALE)
                .phoneNumber(correctPhone)
                .address(addressRequest)
                .build();
        User user = User.builder()
                .id(1L)
                .email(correctEmail)
                .name("홍길동")
                .nickname("길동이")
                .password(correctPwd)
                .gender(Gender.MALE)
                .phoneNumber(correctPhone)
                .address(address)
                .build();

        //when
        when(userRepository.save(any())).thenReturn(user);
        Map<String,String> response = userService.createUser(signupRequest);

        //then
        assertThat(response.get("message")).isEqualTo("success signup");
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void createUserFailByDuplicateEmail(){
        //given
        String existingEmail = "example@gmail.com";
        String correctPwd = "example1234";
        String correctPhone = "010-1234-1234";
        AddressRequest addressRequest = AddressRequest.builder()
                .street("강남대로 123")
                .city("서울 특별시 강남구")
                .zipcode("1010").build();
        SignupRequest signupRequest = SignupRequest.builder()
                .email(existingEmail)
                .name("홍길동")
                .nickname("길동이")
                .password(correctPwd)
                .gender(Gender.MALE)
                .phoneNumber(correctPhone)
                .address(addressRequest)
                .build();

        //when
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        //then
        assertThrows(UserException.class, () -> userService.createUser(signupRequest));

    }
    @Test
    @DisplayName("회원가입 실패 - Server Error (DB)")
    void createUserFailByServerError(){
        // given
        String correctEmail = "example@gmail.com";
        String correctPwd = "example1234";
        String correctPhone = "010-1234-1234";
        AddressRequest addressRequest = AddressRequest.builder()
                .street("강남대로 123")
                .city("서울 특별시 강남구")
                .zipcode("1010").build();
        SignupRequest signupRequest = SignupRequest.builder()
                .email(correctEmail)
                .name("홍길동")
                .nickname("길동이")
                .password(correctPwd)
                .gender(Gender.MALE)
                .phoneNumber(correctPhone)
                .address(addressRequest)
                .build();

        // when
        when(userRepository.existsByEmail(correctEmail)).thenReturn(false);
        when(bCryptPasswordEncoder.encode(correctPwd)).thenReturn(correctPwd);
        when(userRepository.save(any())).thenThrow(new UserException(ErrorCode.CREATE_USER_FAILED));

        // then
        assertThrows(UserException.class, () -> userService.createUser(signupRequest));
    }



}