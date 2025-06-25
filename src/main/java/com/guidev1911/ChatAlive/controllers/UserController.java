package com.guidev1911.ChatAlive.controllers;

import com.guidev1911.ChatAlive.dto.users.UserProfileDTO;
import org.springframework.web.multipart.MultipartFile;
import com.guidev1911.ChatAlive.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @PutMapping("/profile")
    public ResponseEntity<?> updateOwnProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) MultipartFile photoFile) {
        try {
            String email = userDetails.getUsername();
            UserProfileDTO dto = userService.updateOwnProfile(email, name, bio, photoFile);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
