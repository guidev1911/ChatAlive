package com.guidev1911.ChatAlive.services;

import java.util.Collections;
import java.util.Optional;

import com.guidev1911.ChatAlive.model.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.guidev1911.ChatAlive.dto.UserDTO;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService{

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public User register(UserDTO dto) {
        if (repository.existsByEmail(dto.email)) {
            throw new RuntimeException("Email já cadastrado.");
        }

        Role role = dto.role != null ? dto.role : Role.USER;

        User user = new User(dto.name, dto.email, encoder.encode(dto.password), role);
        return repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOpt = repository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        User user = userOpt.get();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(() -> "ROLE_" + user.getRole().name())
        );
    }
}