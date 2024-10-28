package com.example.yesim_spring.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTProvider provider;

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    private String getTokenFromAuthHeader(String authHeader){
        if(authHeader != null && authHeader.startsWith(TOKEN_PREFIX)){
            return authHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HEADER_AUTHORIZATION);
        String jwt = getTokenFromAuthHeader(authHeader);

        if(provider.checkJWT(jwt)){
            Authentication auth = provider.getAuthInfo(jwt);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}