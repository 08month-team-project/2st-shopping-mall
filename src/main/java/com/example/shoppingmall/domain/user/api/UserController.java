package com.example.shoppingmall.domain.user.api;


import com.example.shoppingmall.domain.user.application.UserService;
import com.example.shoppingmall.domain.user.dto.CheckEmailRequest;
import com.example.shoppingmall.domain.user.dto.SignupRequest;
import com.example.shoppingmall.domain.user.dto.SignupResponse;
import com.example.shoppingmall.domain.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest){
        return ResponseEntity.ok(userService.createUser(signupRequest));
    }

    @PostMapping("/check-email")
    public ResponseEntity<UserResponse> checkEmail(@Valid @RequestBody CheckEmailRequest checkEmailRequest){
        return ResponseEntity.ok(userService.checkEmailDuplicate(checkEmailRequest));
    }

}
