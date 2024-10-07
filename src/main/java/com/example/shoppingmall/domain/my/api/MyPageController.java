package com.example.shoppingmall.domain.my.api;

import com.example.shoppingmall.domain.my.application.MyPageService;
import com.example.shoppingmall.domain.my.dto.MyPageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<MyPageRequest> myPage(@Valid @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(myPageService.check(userDetails));
    }
}
