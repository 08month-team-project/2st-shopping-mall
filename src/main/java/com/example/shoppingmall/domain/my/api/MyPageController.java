package com.example.shoppingmall.domain.my.api;

import com.example.shoppingmall.domain.my.application.MyPageService;
import com.example.shoppingmall.domain.my.dto.MyPageRequest;
import com.example.shoppingmall.domain.my.dto.MyPageResponse;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<MyPageResponse> myPage(@AuthenticationPrincipal
                                                    CustomUserDetails userDetails) {
        return ResponseEntity.ok(myPageService.profileCheck(userDetails));
    }

    @PostMapping
    public ResponseEntity<?> modifyMyPage(@AuthenticationPrincipal
                                              CustomUserDetails userDetails
                                                ,MyPageRequest myPageRequest) {
        myPageService.profileModify(userDetails,myPageRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadProfileImage(@AuthenticationPrincipal
                                                    CustomUserDetails userDetails,
                                                @RequestParam("file") MultipartFile file) {
            MyPageService.updateProfileImage(userDetails,file);
            return ResponseEntity.ok().build();
    }
}
