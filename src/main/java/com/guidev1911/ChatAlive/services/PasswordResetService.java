package com.guidev1911.ChatAlive.services;

import com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions.ConfirmationCodeExpiredException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions.InvalidConfirmationCodeException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.userExceptions.UserNotFoundException;
import com.guidev1911.ChatAlive.model.PasswordResetCode;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.repository.PasswordResetCodeRepository;
import com.guidev1911.ChatAlive.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PasswordResetService {

    private final PasswordResetCodeRepository resetCodeRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public PasswordResetService(PasswordResetCodeRepository resetCodeRepository,
                                UserRepository userRepository,
                                EmailService emailService) {
        this.resetCodeRepository = resetCodeRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public void createPasswordResetCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        String code = generateCode();

        PasswordResetCode resetCode = new PasswordResetCode();
        resetCode.setEmail(email);
        resetCode.setCode(code);
        resetCode.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        resetCodeRepository.findByEmail(email).ifPresent(existing -> resetCode.setId(existing.getId()));
        resetCodeRepository.save(resetCode);

        emailService.sendResetCode(email, code);
    }
    public void resetPassword(String email, String code, String newPassword) {
        PasswordResetCode resetCode = resetCodeRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidConfirmationCodeException("Código não encontrado."));

        if (!resetCode.getCode().equals(code)) {
            throw new InvalidConfirmationCodeException("Código inválido.");
        }

        if (resetCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new ConfirmationCodeExpiredException("Código expirado.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        resetCodeRepository.delete(resetCode);
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
