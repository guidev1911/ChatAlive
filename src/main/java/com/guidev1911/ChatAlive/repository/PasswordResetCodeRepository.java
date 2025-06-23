package com.guidev1911.ChatAlive.repository;

import com.guidev1911.ChatAlive.model.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {

    Optional<PasswordResetCode> findByEmail(String email);

    Optional<PasswordResetCode> findByEmailAndCode(String email, String code);
}
