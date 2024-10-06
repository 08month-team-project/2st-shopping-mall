package com.example.shoppingmall.global.config;

import com.example.shoppingmall.global.security.filter.CustomLogoutFilter;
import com.example.shoppingmall.global.security.filter.JwtAuthenticationFilter;
import com.example.shoppingmall.global.security.filter.LoginFilter;
import com.example.shoppingmall.global.security.util.JwtUtil;
import com.example.shoppingmall.global.security.util.RedisAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RedisAuthUtil redisAuthUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.cors((corsCustomizer -> corsCustomizer.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setMaxAge(3600L);
            configuration.setExposedHeaders(Collections.singletonList("Authorization"));
            return configuration;
        })));

        http.formLogin(auth->auth.disable())
                .httpBasic(auth->auth.disable())
                .csrf(auth->auth.disable())
                .cors(auth->auth.disable());



        http.authorizeHttpRequests(auth-> auth
                .requestMatchers(
                        "/",
                        "users/signup",
                        "users/login",
                        "users/check-email",
                        "items/search",
                        "items/categories",
                        "items/size").permitAll()
                .requestMatchers(GET, "items/{item_id}").permitAll()
                .requestMatchers(
                        "items/images/upload",
                        "items/seller/register").hasAuthority("SELLER")
                .anyRequest().authenticated());
        //여기서 중요한 점은, hasRole("SELLER")를 사용할 경우 SELLER 앞에 자동으로 ROLE_이 붙습니다. 즉, Spring Security는 실제로 ROLE_SELLER라는 값을 기대합니다.
        //
        //만약 DB나 토큰에서 SELLER라는 값만 저장하고 있다면, 다음과 같이 변경해야 합니다.
        //
        //코드 복사
        //.requestMatchers(HttpMethod.POST, "/items/seller/register").hasAuthority("SELLER")

        http.addFilterAt(new LoginFilter(jwtUtil, redisAuthUtil, authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, redisAuthUtil),UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, redisAuthUtil), LogoutFilter.class);



        http.sessionManagement(session->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

