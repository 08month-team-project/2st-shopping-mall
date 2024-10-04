package com.example.shoppingmall.global.security.filter;

import com.example.shoppingmall.global.security.util.JwtUtil;
import com.example.shoppingmall.global.security.util.RedisAuthUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;
    private final RedisAuthUtil redisAuthUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request,(HttpServletResponse) response, filterChain);
    }
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        String requestUri = request.getRequestURI();

        if (!requestUri.matches("^||/users/signout$")){
            filterChain.doFilter(request,response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")){
            filterChain.doFilter(request,response);
            return;
        }

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if (cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            jwtUtil.isExpired(refreshToken);
        }catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = jwtUtil.getEmail(refreshToken);
        String redisRefreshToken = redisAuthUtil.getRefreshToken(email);
        try {
            if (jwtUtil.isExpired(redisRefreshToken)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }catch (IllegalArgumentException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!compareRefreshToken(refreshToken,redisRefreshToken)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Cookie cookie = new Cookie("refresh",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        redisAuthUtil.deleteRefreshToken(email);

        response.addCookie(cookie);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"message\":\"로그아웃 성공\"}");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private boolean compareRefreshToken(String cookieToken,String redisToken){
        return cookieToken.equals(redisToken);
    }

}
