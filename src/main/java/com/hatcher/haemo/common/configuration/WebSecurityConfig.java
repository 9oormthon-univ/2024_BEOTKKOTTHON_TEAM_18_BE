package com.hatcher.haemo.common.configuration;

import com.hatcher.haemo.common.jwt.JwtAuthenticationFilter;
import com.hatcher.haemo.common.jwt.JwtExceptionFilter;
import com.hatcher.haemo.user.application.AuthService;
import com.hatcher.haemo.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserService userService;
    private final AuthService authService;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174")); //TODO: 수정 필요
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(
                                new AntPathRequestMatcher("/users/login"),
                                new AntPathRequestMatcher("/users/signup"),
                                new AntPathRequestMatcher("/users/nickname"),
                                new AntPathRequestMatcher("/users/loginId"),
                                //new AntPathRequestMatcher("/users/reissue-token"),
                                new AntPathRequestMatcher("/recruitments/**", "GET"),
                                new AntPathRequestMatcher("/home", "GET")).permitAll() //TODO: 필요 시 추가
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(authService, userService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .build();
    }
}
