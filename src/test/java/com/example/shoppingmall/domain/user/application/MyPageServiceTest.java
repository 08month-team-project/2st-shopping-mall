package com.example.shoppingmall.domain.user.application;

import com.example.shoppingmall.domain.item.application.S3Service;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.dto.MyPageRequest;
import com.example.shoppingmall.domain.user.dto.MyPageResponse;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.domain.user.type.UserRole;
import com.example.shoppingmall.domain.user.type.UserStatus;
import com.example.shoppingmall.global.exception.ErrorCode;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import com.example.shoppingmall.global.security.dto.UserDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private MyPageService myPageService;

    private CustomUserDetails userDetails;
    private User user;
    private UserDetailsDTO userDetailsDTO;

    @BeforeEach
    public void setUp() {
        user = new User(1L,"example@gmail.com","홍길동","길동이","a123456789","010-0000-0000",null, Gender.MALE, UserRole.CUSTOMER, UserStatus.ACTIVE,null,null,null); // 필요한 필드 설정
        userDetailsDTO = UserDetailsDTO.from(user);
        userDetails = new CustomUserDetails(userDetailsDTO); // 필요한 필드 설정
    }

    @Test
    @DisplayName("프로필 조회: 유저가 존재했을때")
    public void testProfileCheck_UserExists() {
        // Given
        when(userRepository.findById(userDetails.getUserId())).thenReturn(Optional.of(user));

        // When
        MyPageResponse response = myPageService.profileCheck(userDetails);

        // Then
        assertNotNull(response);
        assertEquals(user.getName(), response.getName());
        verify(userRepository, times(1)).findById(userDetails.getUserId());
    }
    @Test
    @DisplayName("프로필 조회: 유저가 존재하지 않았을때")
    public void testProfileCheck_UserNotFound() {
        // Given
        when(userRepository.findById(userDetails.getUserId())).thenReturn(Optional.empty());

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            myPageService.profileCheck(userDetails);
        });
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("프로필 수정: 유저가 존재했을때")
    public void testProfileModify_UserExists() {
        // Given
        MyPageRequest myPageRequest = MyPageRequest.builder()
                .name("홍길동")
                .nickname("길동이")
                .phone("010-0000-0000")
                .email("example@gmail.com")
                .password("a123456789")
                .gender(Gender.MALE)
                .build();
        when(userRepository.findById(userDetails.getUserId())).thenReturn(Optional.of(user));

        // When
        myPageService.profileModify(userDetails, myPageRequest);

        // Then
        verify(userRepository, times(1)).findById(userDetails.getUserId());
        assertEquals(myPageRequest.getName(), user.getName());

    }
    @Test
    @DisplayName("프로필이미지: 유저가 존재했을때")
    public void testUpdateProfileImage_UserExists() {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(userRepository.findById(userDetails.getUserId())).thenReturn(Optional.of(user));
        when(s3Service.uploadValidation(file)).thenReturn("uploadedImageUrl");

        // When
        myPageService.updateProfileImage(userDetails, file);

        // Then
        verify(userRepository, times(1)).findById(userDetails.getUserId());
        verify(s3Service, times(1)).uploadValidation(file);
        assertEquals("uploadedImageUrl", user.getProfileImageUrl());
    }

    @Test
    @DisplayName("프로필이미지: 유저가 존재하지 않았을때")
    public void testUpdateProfileImage_UserNotFound() {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(userRepository.findById(userDetails.getUserId())).thenReturn(Optional.empty());

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            myPageService.updateProfileImage(userDetails, file);
        });
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}