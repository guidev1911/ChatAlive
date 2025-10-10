package com.guidev1911.ChatAlive.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import com.guidev1911.ChatAlive.Role.UserRole;
import com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions.ConfirmationCodeAlreadySentException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions.ConfirmationCodeExpiredException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions.EmailAlreadyRegisteredException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions.InvalidConfirmationCodeException;
import com.guidev1911.ChatAlive.mapper.UserMapper;
import com.guidev1911.ChatAlive.model.PendingUser;
import com.guidev1911.ChatAlive.repository.PendingUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.guidev1911.ChatAlive.dto.users.UserDTO;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private PendingUserRepository pendingUserRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserMapper userMapper;

    public void register(UserDTO dto) {
        if (repository.existsByEmail(dto.email)) {
            throw new EmailAlreadyRegisteredException("Email já cadastrado.");
        }

        if (pendingUserRepository.findByEmail(dto.email).isPresent()) {
            throw new ConfirmationCodeAlreadySentException("Já existe um código de confirmação pendente para esse e-mail. Aguarde 5 minutos e tente novamente.");
        }

        String confirmationCode = String.valueOf(new Random().nextInt(900000) + 100000);

        PendingUser pending = userMapper.toPendingUser(dto);
        pending.setRole(dto.role != null ? dto.role : UserRole.USER);
        pending.setEncodedPassword(encoder.encode(dto.password));
        pending.setConfirmationCode(confirmationCode);
        pending.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        pendingUserRepository.save(pending);
        emailService.sendConfirmationEmail(dto.email, confirmationCode);
    }

    @Transactional
    public User confirmEmail(String email, String code) {
        PendingUser pending = pendingUserRepository
                .findByEmailAndConfirmationCode(email, code)
                .orElseThrow(() -> new InvalidConfirmationCodeException("Código inválido ou e-mail incorreto."));

        if (pending.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new ConfirmationCodeExpiredException("Código expirado.");
        }

        User user = userMapper.toUser(pending);
        pendingUserRepository.deleteByEmail(email);
        return repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(() -> "ROLE_" + user.getRole().name())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
