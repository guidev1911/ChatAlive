package com.guidev1911.ChatAlive.services;

import com.guidev1911.ChatAlive.dto.users.UserProfileDTO;
import com.guidev1911.ChatAlive.exception.customizedExceptions.userExceptions.ImageStorageException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.userExceptions.UserNotFoundException;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UserService{

    @Autowired
    private UserRepository repository;


    public UserProfileDTO updateOwnProfile(String email, String name, String bio, MultipartFile photoFile) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (name != null && !name.trim().isEmpty()) {
            user.setName(name);
        }

        if (bio != null && !bio.trim().isEmpty()) {
            user.setBio(bio);
        }

        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(photoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                user.setPhotoUrl("/uploads/" + fileName);
            } catch (IOException e) {
                throw new ImageStorageException("Erro ao salvar a imagem", e);
            }
        }

        repository.save(user);

        return new UserProfileDTO(user.getName(), user.getBio(), user.getPhotoUrl());
    }
}