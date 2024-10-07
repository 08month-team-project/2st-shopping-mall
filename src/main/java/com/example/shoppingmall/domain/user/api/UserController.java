package com.example.shoppingmall.domain.user.api;


import com.example.shoppingmall.domain.user.application.UserService;
import com.example.shoppingmall.domain.user.dto.CheckEmailRequest;
import com.example.shoppingmall.domain.user.dto.SignupRequest;
import com.example.shoppingmall.domain.user.dto.SignupResponse;
import com.example.shoppingmall.domain.user.dto.UserResponse;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


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

    @PatchMapping("/status/inactive")
    public ResponseEntity<UserResponse> inactiveUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     HttpServletResponse servletResponse){
        UserResponse userResponse = userService.inactiveUser(userDetails);

        SecurityContextHolder.clearContext();
        Cookie cookie = new Cookie("refresh",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        servletResponse.addCookie(cookie);
        return ResponseEntity.ok(userResponse);
    }

}
