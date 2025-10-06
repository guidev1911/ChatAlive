package com.guidev1911.ChatAlive.dto.groups;


import java.util.List;

public record GroupDTO(
        Long id,
        String name,
        String description,
        String privacy,
        String creatorEmail,
        String groupImageUrl,
        List<GroupMembershipDTO> members
) {}