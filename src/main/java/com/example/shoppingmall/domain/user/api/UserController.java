package com.example.shoppingmall.domain.user.api;


import com.example.shoppingmall.domain.user.application.UserService;
import com.example.shoppingmall.domain.user.dto.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String,String>> signup(@Valid @RequestBody SignupRequest signupRequest){
        Map<String,String> response = userService.createUser(signupRequest);
        return ResponseEntity.ok(response);
    }
}
