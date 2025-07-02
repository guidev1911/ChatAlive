package com.guidev1911.ChatAlive.controllers;

import com.guidev1911.ChatAlive.dto.responses.ApiResponse;
import com.guidev1911.ChatAlive.dto.email.ConfirmCodeDTO;
import com.guidev1911.ChatAlive.dto.responses.TokenResponse;
import com.guidev1911.ChatAlive.dto.users.UserDTO;
import com.guidev1911.ChatAlive.secutiry.JwtUtil;
import com.guidev1911.ChatAlive.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> register(@RequestBody UserDTO dto) {
        authService.register(dto);
        return ResponseEntity.ok(new ApiResponse(true,"Código de confirmação enviado para seu e-mail."));
    }
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse> confirm(@RequestBody ConfirmCodeDTO confirmDTO) {
        authService.confirmEmail(confirmDTO.getEmail(), confirmDTO.getCode());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Usuário registrado com sucesso!"));
    }



}
