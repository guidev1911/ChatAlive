package com.guidev1911.ChatAlive.services;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.guidev1911.ChatAlive.dto.UserDTO;
import com.guidev1911.ChatAlive.dto.UserUpdateDTO;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public User register(UserDTO dto) {
        if (repo.existsByEmail(dto.email)) {
            throw new RuntimeException("Email já cadastrado.");
        }
        User user = new User(dto.nome, dto.email, encoder.encode(dto.senha), dto.role);
        return repo.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public User updateUser(Long id, UserUpdateDTO dto) {
        User user = repo.findById(id).orElseThrow();
        user.setNome(dto.nome);
        user.setSenha(encoder.encode(dto.senha));
        return repo.save(user);
    }

    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOpt = repo.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        User user = userOpt.get();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getSenha(),
                Collections.singletonList(() -> "ROLE_" + user.getRole().name())
        );
    }
}