
package com.ebuka.notesapi;

import com.ebuka.notesapi.service.JwtAuthenticationFilter;
import com.ebuka.notesapi.service.JwtService;
import tools.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtService jwtService,
            ObjectMapper objectMapper) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            .headers(headers ->
                headers.frameOptions(frame ->
                    frame.sameOrigin()
                )
            )

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(
                    (request, response, authException) -> {
                        response.setStatus(401);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        objectMapper.writeValue(
                            response.getOutputStream(),
                            new com.ebuka.notesapi.dto.res.ApiRes<Void>(
                                false,
                                "Unauthorized",
                                null
                            )
                        );
                    }
                )
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                new JwtAuthenticationFilter(jwtService, objectMapper),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
