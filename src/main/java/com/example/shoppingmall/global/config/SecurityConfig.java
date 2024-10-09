package com.example.shoppingmall.global.config;

import com.example.shoppingmall.global.security.filter.CustomLogoutFilter;
import com.example.shoppingmall.global.security.filter.JwtAuthenticationFilter;
import com.example.shoppingmall.global.security.filter.LoginFilter;
import com.example.shoppingmall.global.security.util.JwtUtil;
import com.example.shoppingmall.global.security.util.RedisAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RedisAuthUtil redisAuthUtil;

    @Value("${custom.url}")
    private String awsUrl;


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

//        http.cors((corsCustomizer -> corsCustomizer.configurationSource(request -> {
//            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//            configuration.setAllowedOrigins(Collections.singletonList(awsUrl));
//            configuration.setAllowedMethods(Collections.singletonList("*"));
//            configuration.setAllowCredentials(true);
//            configuration.setAllowedHeaders(Collections.singletonList("*"));
//            configuration.setAllowedMethods(Collections.singletonList("POST"));
//            configuration.setAllowedMethods(Collections.singletonList("GET"));
//            configuration.setMaxAge(3600L);
//            configuration.setExposedHeaders(Collections.singletonList("Authorization"));
//            return configuration;
//        })));

        http.formLogin(auth->auth.disable())
                .httpBasic(auth->auth.disable())
                .csrf(auth->auth.disable());


        http.authorizeHttpRequests(auth-> auth
                .requestMatchers(
                        "/**").permitAll()
                .anyRequest().authenticated());


        http.addFilterAt(new LoginFilter(jwtUtil, redisAuthUtil, authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, redisAuthUtil),UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, redisAuthUtil), LogoutFilter.class);



        http.sessionManagement(session->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

