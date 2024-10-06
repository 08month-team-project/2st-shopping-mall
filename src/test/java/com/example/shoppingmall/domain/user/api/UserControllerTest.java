package com.example.shoppingmall.domain.user.api;

import com.example.shoppingmall.domain.user.application.UserService;
import com.example.shoppingmall.domain.user.dto.*;
import com.example.shoppingmall.domain.user.type.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    private SignupRequest signupRequest;
    @BeforeEach
    public void setUp(){
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        AddressRequest addressRequest = AddressRequest.builder()
                .city("서울 특별시 강남구 강남대로 123")
                .zipcode("1010").build();
        signupRequest = SignupRequest.builder()
                .email("example@gmail.com")
                .name("홍길동")
                .nickname("길동이")
                .password("mypass1234")
                .gender(Gender.MALE)
                .phoneNumber("010-1234-1234")
                .address(addressRequest)
                .build();
    }

    @Test
    @DisplayName("POST 회원 가입 성공")
    void signup() throws Exception {
        //given
        SignupResponse response = SignupResponse.builder().message("success signup").build();

        //when
        when(userService.createUser(any())).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(post("/users/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                .value(response.getMessage()));
    }
    @Test
    @DisplayName("POST 이메일 중복 체크 성공")
    void checkEmailSuccess() throws Exception {
        //give
        CheckEmailRequest request = CheckEmailRequest.builder()
                .email("example1234@gmail.com").build();

        UserResponse response = UserResponse.builder()
                .message("사용 가능한 이메일입니다.").build();

        //when
        doReturn(response).when(userService).checkEmailDuplicate(any(CheckEmailRequest.class));
        ResultActions resultActions = mockMvc.perform(post("/users/check-email")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value(response.getMessage()));

    }

}