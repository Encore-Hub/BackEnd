package com.team6.backend.security.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // csrf라는 보안을 잠깐 해지함

                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        //오리진 허용
                        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173"));
                        //모든 http 메서드 허용
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        // 자격 증명허용
                        configuration.setAllowCredentials(true);
                        // 모든 헤더 적용
                        configuration.setAllowedHeaders(Collections.singletonList("*"));

                        configuration.setMaxAge(3600L);
                        // 노출한 헤더 설정
                        configuration.setExposedHeaders(Arrays.asList("AccessToken", "RefreshToken"));
                        return configuration;

                    }
                })))

                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/member/**").permitAll()
                                .anyRequest().authenticated()
                )
        ;

        return http.build();
    }
}
