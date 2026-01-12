package com.storix.storix_api.global.config;

import com.storix.storix_api.global.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final OnboardingAuthenticationFilter onboardingFilter;
    private final ErrorHandlingFilter errorHandlingFilter;

    private final SecurityEntryPoint securityEntryPoint;
    private final SecurityDeniedHandler securityDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                // [Auth]
                                .requestMatchers("/api/v1/auth/oauth/**").permitAll()
                                .requestMatchers("/api/v1/auth/nickname/valid").permitAll()
                                .requestMatchers("/api/v1/auth/users/artist/login").permitAll()
                                .requestMatchers("/api/v1/auth/tokens/refresh").permitAll()
                                .requestMatchers("/api/v1/auth/developer/**").permitAll() // 추후 Admin 변경
                                // [Search]
                                .requestMatchers("/api/v1/search/**").permitAll()
                                // [Profile]
                                .requestMatchers("/api/v1/profile/nickname/**").hasRole("READER")
                                .requestMatchers("/api/v1/profile/**").hasAnyRole("READER","ARTIST")
                                // [Image]
                                .requestMatchers("/api/v1/image/fan-board").hasRole("ARTIST")

                                .anyRequest().authenticated()
                )

                // jwt filter
                .addFilterBefore(errorHandlingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(onboardingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // spring security exception handler
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(securityEntryPoint) // 401 error
                        .accessDeniedHandler(securityDeniedHandler) // 403 error
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
                "https://storix.kr",
                "https://www.storix.kr",
                "https://api.storix.kr",
                "http://localhost:3000",
                "https://storix-fe-git-develop-kim-yunseongs-projects.vercel.app",
                "https://storix-fe-git-main-kim-yunseongs-projects.vercel.app"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        config.setAllowCredentials(true);
        config.addExposedHeader("Set-Cookie");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
