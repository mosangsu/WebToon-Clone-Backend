package com.lezhin.clone.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lezhin.clone.backend.filter.JwtRequestFilter;
import com.lezhin.clone.backend.handler.OAuth2AuthenticationSuccessHandler;
import com.lezhin.clone.backend.service.OAuth2Service;
import com.lezhin.clone.backend.user.HttpCookieOAuth2AuthorizationRequestRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {
    
    private final JwtRequestFilter jwtRequestFilter;
    private final OAuth2Service oAuth2Service;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
 
    // 사용자 계정 패스워드 인코더
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // 시큐리티 접근 권한 관리
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req
                .requestMatchers("/user/login").permitAll()
                .requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll()
            )
            .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2Login(configure -> 
                configure.authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))    // 소셜 로그인 페이지로 리다이렉트 전 인증 요청에 필요한 정보 저장
                .userInfoEndpoint(config -> config.userService(oAuth2Service))  // Access 토큰 획득 이후 사용자 정보 조회. 이후 로그인 or 회원가입 분기처리를 위한 전처리 작업 수행
                .successHandler(oAuth2AuthenticationSuccessHandler) // 로그인 성공
                // .failureHandler(oAuth2AuthenticationFailureHandler)  // 로그인 실패
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}