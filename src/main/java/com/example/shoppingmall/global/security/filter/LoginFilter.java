package com.example.shoppingmall.global.security.filter;

import com.example.shoppingmall.domain.user.dto.LoginRequest;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import com.example.shoppingmall.global.security.util.JwtUtil;
import com.example.shoppingmall.global.security.util.RedisAuthUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final RedisAuthUtil redisAuthUtil;
    private final AuthenticationManager authenticationManager;

    public LoginFilter(JwtUtil jwtUtil, RedisAuthUtil redisAuthUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.redisAuthUtil = redisAuthUtil;
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            if (!request.getMethod().equals("POST")){
                response.setContentType("application/json; charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"message\":\"요청 메소드가 올바르지 않습니다.\"}");
                return null;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            if (!isEmailValid(email)) {
                response.setContentType("application/json; charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"message\":\"이메일 형식이 올바르지 않습니다.\"}");
                return null;
            }

            if (!isPasswordValid(password)) {
                response.setContentType("application/json; charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"message\":\"비밀번호는 영문자, 숫자 조합 8자 이상, 20자 이하를 사용하세요.\"}");
                return null;
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email,password,null);
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            response.setContentType("application/json; charset=UTF-8");
            try {
                response.getWriter().write("{\"message\":\"사용자 인증에 실패했습니다.\"}");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            response.setStatus(401);
            return null;
        }
    }
    private boolean isEmailValid(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.find();
    }

    private boolean isPasswordValid(String password) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{8,20}$");
        Matcher passwordMatcher = passwordPattern.matcher(password);
        return passwordMatcher.find();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        Long userId = customUserDetails.getUserId();
        String userEmail = customUserDetails.getUsername();
        String name = customUserDetails.getName();
        String nickname = customUserDetails.getNickname();
        String gender = customUserDetails.getGender();
        String phoneNumber = customUserDetails.getPhoneNumber();
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();
        String role = authority.getAuthority();

        String accessToken = jwtUtil.createJwt("access",userId,userEmail,role, 1000*60*60L);
        String refreshToken = jwtUtil.createJwt("refresh",userId,userEmail,role, 1000*60*60*24*3L);
        redisAuthUtil.saveRefreshToken(userEmail,refreshToken);

        Cookie refreshCookie = new Cookie("refresh",refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 3);
        response.addCookie(refreshCookie);

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{" +
                "\"message\":\"로그인 성공\"," +
                "\"email\":\"" + userEmail + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"nickname\":\"" + nickname + "\"," +
                "\"gender\":\"" + gender + "\"," +
                "\"phone\":\"" + phoneNumber + "\"" +
                "}");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Authorization","Bearer "+accessToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"message\":\"사용자 인증에 실패했습니다.\"}");
        response.setStatus(401);
    }

}
