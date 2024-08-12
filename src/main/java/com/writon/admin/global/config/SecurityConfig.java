package com.writon.admin.global.config;

import com.writon.admin.global.config.auth.JwtAccessDeniedHandler;
import com.writon.admin.global.config.auth.JwtAuthenticationEntryPoint;
import com.writon.admin.global.config.auth.JwtFilter;
import com.writon.admin.global.config.auth.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final TokenProvider tokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // token을 사용하기 때문에 csrf를 disable함
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)

        // 예외 핸들링
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)
        )

        // session을 사용하지 않기 때문에 STATELESS로 설정
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        // HttpServletRequest를 사용하는 요청들에 대한 접근제한 설정
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/auth/login", "/auth/signup").permitAll()
            .anyRequest().authenticated()
        )

        // JwtFilter로 인터셉트하는 과정
        .addFilterBefore(
            new JwtFilter(tokenProvider),
            UsernamePasswordAuthenticationFilter.class
        );

    return http.build();
  }

}