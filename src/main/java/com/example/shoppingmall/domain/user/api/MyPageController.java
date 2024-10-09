package com.example.shoppingmall.domain.user.api;

import com.example.shoppingmall.domain.user.application.MyPageService;
import com.example.shoppingmall.domain.user.dto.MyPageRequest;
import com.example.shoppingmall.domain.user.dto.MyPageResponse;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"},
        allowCredentials = "true",maxAge = 3600,
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PATCH,RequestMethod.PUT,RequestMethod.OPTIONS},
        exposedHeaders = {"Authorization","Content-Type"})
@RequestMapping("/users/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<MyPageResponse> myPage(@AuthenticationPrincipal
                                                    CustomUserDetails userDetails) {
        return ResponseEntity.ok(myPageService.profileCheck(userDetails));
    }

    @PostMapping
    public ResponseEntity<Void> modifyMyPage(@AuthenticationPrincipal
                                              CustomUserDetails userDetails
                                                , MyPageRequest myPageRequest) {
        myPageService.profileModify(userDetails,myPageRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public ResponseEntity<Void> uploadProfileImage(@AuthenticationPrincipal
                                                    CustomUserDetails userDetails,
                                                @RequestParam("file") MultipartFile file) {
        myPageService.updateProfileImage(userDetails,file);
        return ResponseEntity.ok().build();
    }
}
