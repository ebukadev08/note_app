
package com.ebuka.notesapi;

import com.ebuka.notesapi.repository.UserRepository;
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
            UserRepository userRepository,
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

            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    objectMapper.writeValue(
                        response.getOutputStream(),
                        com.ebuka.notesapi.dto.res.ApiRes.error("Unauthorized")
                    );
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    objectMapper.writeValue(
                        response.getOutputStream(),
                        com.ebuka.notesapi.dto.res.ApiRes.error("Access denied")
                    );
                })
            )
            

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // .requestMatchers("/api/wallet/**").hasRole("USER")
                .anyRequest().authenticated()
            )

            .addFilterBefore(
            new JwtAuthenticationFilter(
                    jwtService,
                    userRepository,
                    objectMapper
            ),
            UsernamePasswordAuthenticationFilter.class
    );

        return http.build();
    }
}
