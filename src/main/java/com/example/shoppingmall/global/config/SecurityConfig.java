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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

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
            configuration.setAllowedOrigins(List.of("http://localhost:3000"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
            configuration.setAllowCredentials(true);
            configuration.setMaxAge(3600L);
            configuration.setExposedHeaders(Arrays.asList("Authorization","Content-Type"));
            return configuration;
        })));

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

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:3000");
//        config.addAllowedHeader("");
//        config.addAllowedMethod("");
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
}

