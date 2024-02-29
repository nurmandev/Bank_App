package com.bank.bankapp.config;

import org.springframework.web.filter.OncePerRequestFilter;

import com.bank.bankapp.dto.UserRequest;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
    
    private JwtTokenProvider jwtTokenProvider;
    private UserRequest userRequest;


}
