package com.guidev1911.ChatAlive.dto.groups;

public record GroupMembershipDTO(
        Long id,
        String userEmail,
        String role,
        boolean pendingRequest
) {}