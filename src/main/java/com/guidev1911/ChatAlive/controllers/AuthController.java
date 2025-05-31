package com.guidev1911.ChatAlive.controllers;

import com.guidev1911.ChatAlive.dto.UserDTO;
import com.guidev1911.ChatAlive.secutiry.JwtUtil;
import com.guidev1911.ChatAlive.services.UserService;
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
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody UserDTO dto) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
        UserDetails userDetails = userService.loadUserByUsername(dto.getEmail());
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/register")
    public String register(@RequestBody UserDTO dto) {
        userService.register(dto);
        return "Usuário registrado com sucesso!";
    }
}
