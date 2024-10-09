package com.example.shoppingmall.global.security.filter;

import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import com.example.shoppingmall.global.security.dto.UserDetailsDTO;
import com.example.shoppingmall.global.security.util.JwtUtil;
import com.example.shoppingmall.global.security.util.RedisAuthUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisAuthUtil redisAuthUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.extractAccessToken(request);
        String refreshTokenFromCookie = jwtUtil.extractRefreshToken(request.getCookies());
        try {
            if (StringUtils.hasText(accessToken) && !jwtUtil.isExpired(accessToken)) {
                if (!jwtUtil.getCategory(accessToken).equals("access")) {
                    log.info("Invalid token category");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write("{\"message\":\"토큰 카테고리가 일치하지 않습니다.\"}");
                    return;
                }
                if (!validateRefreshToken(refreshTokenFromCookie)){
                    log.info("리프레시 = {}",refreshTokenFromCookie);
                    log.info("액세스 토큰은 유효한데 리프레시 토큰이 유효하지 않습니다.");
                    expiredRefreshTokenResponse(response);
                    return;
                }
                authenticateWithAccessToken(accessToken);
            }
        } catch (ExpiredJwtException e) {
            log.info("액세스 토큰이 만료되었습니다: {}", e.getMessage());
            try {
                if (validateRefreshToken(refreshTokenFromCookie)) {
                    String userEmail = jwtUtil.getEmail(refreshTokenFromCookie);
                    Long userId = jwtUtil.getId(refreshTokenFromCookie);
                    String refreshTokenFromRedis = redisAuthUtil.getRefreshToken(userEmail);
                    if (compareRefreshToken(refreshTokenFromCookie, refreshTokenFromRedis)) {
                        String newAccessToken = jwtUtil.createJwt("access", userId, userEmail, jwtUtil.getRole(refreshTokenFromCookie), 1000*60*60L);
                        response.setHeader("Authorization", "Bearer " + newAccessToken);
                        authenticateWithAccessToken(newAccessToken);
                    } else {
                        notEqualsRefreshTokenResponse(response);
                        return;
                    }
                }
            }catch (ExpiredJwtException e2){
                log.info("리프레시 토큰이 유효하지 않습니다. {}", e2.getMessage());
                expiredRefreshTokenResponse(response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    private void expiredRefreshTokenResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"message\":\"리프레시 토큰이 만료되었거나 유효하지 않습니다. 다시 로그인 해주세요.\"}");
    }
    private void notEqualsRefreshTokenResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"message\":\"리프레시 토큰이 일치하지 않습니다. 다시 로그인 해주세요.\"}");
    }

    private void authenticateWithAccessToken(String accessToken) {
        UserDetailsDTO userDetailsDTO = UserDetailsDTO.builder()
                .userId(jwtUtil.getId(accessToken))
                .email(jwtUtil.getEmail(accessToken))
                .role(jwtUtil.getRole(accessToken))
                .build();

        SecurityContextHolder.getContext().setAuthentication(authenticate(userDetailsDTO));
    }

    private boolean validateRefreshToken(String refreshToken) {
        return StringUtils.hasText(refreshToken) && !jwtUtil.isExpired(refreshToken);
    }

    private boolean compareRefreshToken(String cookieToken, String redisToken) {
        return cookieToken.equals(redisToken);
    }

    private Authentication authenticate(UserDetailsDTO userDetailsDTO) {
        CustomUserDetails customUserDetails = new CustomUserDetails(userDetailsDTO);
        return new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());
    }
}
