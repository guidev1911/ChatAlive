package com.guidev1911.ChatAlive.repository;

import com.guidev1911.ChatAlive.model.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingUserRepository extends JpaRepository<PendingUser, Long> {
    Optional<PendingUser> findByEmail(String email);
    Optional<PendingUser> findByEmailAndConfirmationCode(String email, String code);
    void deleteByEmail(String email);
}

