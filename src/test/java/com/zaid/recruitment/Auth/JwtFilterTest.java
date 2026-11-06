package com.zaid.recruitment.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    private JwtService jwtService;
    private JwtFilter jwtFilter;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        jwtFilter = new JwtFilter(jwtService);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateCandidateWithValidToken()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");

        MockHttpServletResponse response = new MockHttpServletResponse();

        Claims claims = mock(Claims.class);
        when(jwtService.parseToken("valid-token")).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(claims.get("role", String.class)).thenReturn("CANDIDATE");
        when(claims.getSubject()).thenReturn("candidate");

        jwtFilter.doFilter(request, response, filterChain);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals("candidate", authentication.getName());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE")));

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldContinueWhenAuthorizationHeaderIsMissing()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldContinueWhenHeaderIsNotBearerToken()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic abc123");

        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldReturnUnauthorizedWhenTokenIsInvalid()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-token");

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.parseToken("invalid-token"))
                .thenThrow(new JwtException("Invalid token"));

        jwtFilter.doFilter(request, response, filterChain);

        assertEquals(401, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenClaimsAreInvalid()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer expired-token");

        MockHttpServletResponse response = new MockHttpServletResponse();

        Claims claims = mock(Claims.class);
        when(jwtService.parseToken("expired-token")).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(false);

        jwtFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(200, response.getStatus());
        verify(filterChain).doFilter(request, response);
    }
}