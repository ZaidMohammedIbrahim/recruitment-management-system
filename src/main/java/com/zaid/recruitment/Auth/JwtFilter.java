package com.zaid.recruitment.Auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@Component
public class JwtFilter extends OncePerRequestFilter {
    JwtService jwtService;
    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // No JWT provided: continue normally.
        // Public endpoints can proceed; protected endpoints are checked later.
        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            Claims claims = jwtService.parseToken(token);

            if (jwtService.validateToken(claims)) {
                String role = claims.get("role", String.class);
                String username = claims.getSubject();

                var authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + role)
                );

                var authentication =
                        new UsernamePasswordAuthenticationToken(
                                username, null, authorities
                        );

                authentication.setDetails(new WebAuthenticationDetails(request));

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
            return;
        }

        filterChain.doFilter(request, response);}}