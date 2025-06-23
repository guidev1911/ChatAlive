package com.guidev1911.ChatAlive.controllers;

import com.guidev1911.ChatAlive.dto.ApiResponse;
import com.guidev1911.ChatAlive.dto.EmailRequest;
import com.guidev1911.ChatAlive.dto.PasswordResetRequest;
import com.guidev1911.ChatAlive.services.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password-reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse> requestReset(@RequestBody @Valid EmailRequest request) {
        passwordResetService.createPasswordResetCode(request.getEmail());
        return ResponseEntity.ok(new ApiResponse(true, "Código de redefinição enviado para o e-mail."));
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse> confirmReset(@RequestBody @Valid PasswordResetRequest request) {
        passwordResetService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Senha redefinida com sucesso."));
    }

}