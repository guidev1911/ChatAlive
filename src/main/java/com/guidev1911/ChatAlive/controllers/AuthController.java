package com.guidev1911.ChatAlive.controllers;

import com.guidev1911.ChatAlive.dto.TokenResponse;
import com.guidev1911.ChatAlive.dto.UserDTO;
import com.guidev1911.ChatAlive.secutiry.JwtUtil;
import com.guidev1911.ChatAlive.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/login")
    public TokenResponse login(@RequestBody UserDTO dto) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        UserDetails userDetails = authService.loadUserByUsername(dto.getEmail());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        return new TokenResponse(token);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserDTO dto) {
        authService.register(dto);
        return "Usuário registrado com sucesso!";
    }
}
