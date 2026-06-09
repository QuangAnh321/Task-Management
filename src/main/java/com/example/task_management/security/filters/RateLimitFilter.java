package com.example.task_management.security.filters;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.task_management.services.RateLimiterService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;
    private final JwtDecoder jwtDecoder;

    public RateLimitFilter(RateLimiterService rateLimiterService, JwtDecoder jwtDecoder) {
        this.rateLimiterService = rateLimiterService;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String userEmail = extractUserEmailFromRequest(request);
        if (userEmail != null && !rateLimiterService.isAllowed(userEmail)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Please try again later.");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String extractUserEmailFromRequest(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null; // No token provided
        }
        String token = authorizationHeader.substring(7);
        // Decode the JWT token and extract the user email claim
        String userEmail = jwtDecoder.decode(token).getClaim("email");
        return userEmail; // Return the extracted user email
    }
    
}
