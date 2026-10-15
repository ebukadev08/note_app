
package com.ebuka.notesapi.service;

import com.ebuka.notesapi.dto.res.ApiRes;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // No token: allow Spring Security to decide access.
        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // A supplied Authorization header must use Bearer authentication.
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

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email, null, List.of());

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
