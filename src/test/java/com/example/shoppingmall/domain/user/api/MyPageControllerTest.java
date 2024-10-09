package com.example.shoppingmall.domain.user.api;

import com.example.shoppingmall.domain.user.application.MyPageService;
import com.example.shoppingmall.domain.user.dto.MyPageRequest;
import com.example.shoppingmall.domain.user.dto.MyPageResponse;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class MyPageControllerTest {

   @InjectMocks
   private MyPageController myPageController;

    private MockMvc mockMvc;

    @Mock
    private MyPageService myPageService;

    @Mock
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(myPageController).build();
        userDetails = Mockito.mock(CustomUserDetails.class);
    }

    @Test
    @DisplayName("프로필 조회")
    void myPage() throws Exception {
        // given
        MyPageResponse response = new MyPageResponse("홍길동","닉네임","asdf@gmail.com","010-0000-0000", Gender.MALE,null,null,null);  // 필요에 맞게 더미 데이터를 넣으세요.

        when(myPageService.profileCheck(any(CustomUserDetails.class))).thenReturn(response);

        ResponseEntity<MyPageResponse> result = myPageController.myPage(userDetails);
        // when & then
        assertThat(result.getStatusCodeValue()).isEqualTo(200);  // HTTP 200 OK
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("프로필 수정")
    void modifyMyPage() throws Exception {
        MyPageRequest myPageRequest = new MyPageRequest();

        ResponseEntity<Void> result = myPageController.modifyMyPage(userDetails, myPageRequest);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @DisplayName("이미지 업로드")
    void uploadProfileImage() {
        MultipartFile file = new MockMultipartFile(
                "file",  // 요청 파라미터 이름
                "test.png",  // 원본 파일 이름
                MediaType.IMAGE_PNG_VALUE,  // MIME 타입
                "image content".getBytes()  // 파일 내용 (바이트 배열)
        );
        ResponseEntity<Void> result = myPageController.uploadProfileImage(userDetails, file);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
    }
}