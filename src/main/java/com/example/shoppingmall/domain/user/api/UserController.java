package com.example.shoppingmall.domain.user.api;


import com.example.shoppingmall.domain.user.application.UserService;
import com.example.shoppingmall.domain.user.dto.CheckEmailRequest;
import com.example.shoppingmall.domain.user.dto.SignupRequest;
import com.example.shoppingmall.domain.user.dto.SignupResponse;
import com.example.shoppingmall.domain.user.dto.UserResponse;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = {"http://localhost:3000"},
        allowCredentials = "true",maxAge = 3600,
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PATCH,RequestMethod.PUT,RequestMethod.OPTIONS},
        exposedHeaders = {"Authorization","Content-Type"})
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest){
        return ResponseEntity.ok(userService.createUser(signupRequest));
    }

    @Operation(summary = "이메일 체크")
    @PostMapping("/check-email")
    public ResponseEntity<UserResponse> checkEmail(@Valid @RequestBody CheckEmailRequest checkEmailRequest){
        return ResponseEntity.ok(userService.checkEmailDuplicate(checkEmailRequest));
    }

    @Operation(summary = "비활성 사용자")
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

    @Operation(summary = "판매자 전환")
    @PatchMapping("/role/seller")
    public ResponseEntity<UserResponse> changeRole(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(userService.changeRoleSeller(userDetails));
    }

}
