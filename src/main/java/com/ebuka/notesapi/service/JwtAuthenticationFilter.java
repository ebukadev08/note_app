
package com.ebuka.notesapi.service;

import com.ebuka.notesapi.dto.res.ApiRes;
import com.ebuka.notesapi.repository.UserRepository;
import tools.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository,
            ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authHeader.startsWith("Bearer ")) {
            sendUnauthorized(response, "Invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        if (token.isBlank()) {
            sendUnauthorized(response, "Token is required");
            return;
        }

        try {
            if (!jwtService.isTokenValid(token)) {
                sendUnauthorized(response, "Invalid or expired token");
                return;
            }

            String email = jwtService.extractEmail(token);

            var user = userRepository.findByEmail(email)
                    .orElse(null);

            if (user == null || !user.isVerified()) {
                sendUnauthorized(response, "Invalid or unavailable account");
                return;
            }

            var authority = new SimpleGrantedAuthority(
                    "ROLE_" + user.getRole().name()
            );

            var authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(authority)
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException
                 | ResponseStatusException exception) {

            SecurityContextHolder.clearContext();
            sendUnauthorized(response, "Invalid or expired token");
        }
    }

    private void sendUnauthorized(
            HttpServletResponse response,
            String message) throws IOException {

        SecurityContextHolder.clearContext();

        if (response.isCommitted()) {
            return;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiRes<Void> apiResponse = new ApiRes<>(
                false,
                message,
                null
        );

        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}