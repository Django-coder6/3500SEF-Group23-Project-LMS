package com.group23.config;

import com.group23.security.JwtAuthenticationFilter;
import com.group23.security.RestAccessDeniedHandler;
import com.group23.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/health").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/parcels")
                        .hasAnyRole("STAFF", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/parcels", "/api/parcels/batch")
                        .hasRole("STAFF")
                        .requestMatchers(HttpMethod.PATCH, "/api/parcels/*/status")
                        .hasRole("STAFF")

                        .requestMatchers(HttpMethod.GET, "/api/my-parcels", "/api/my-parcels/**")
                        .hasRole("RESIDENT")

                        .requestMatchers(HttpMethod.GET, "/api/locations")
                        .hasAnyRole("STAFF", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/locations/recommend")
                        .hasRole("STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/pickup/verify", "/api/pickup/confirm")
                        .hasRole("STAFF")

                        .anyRequest().denyAll());

        return http.build();
    }
}
