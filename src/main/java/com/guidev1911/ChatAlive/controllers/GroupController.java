package com.guidev1911.ChatAlive.controllers;

import com.guidev1911.ChatAlive.dto.responses.ApiResponse;
import com.guidev1911.ChatAlive.dto.groups.CreateGroupRequest;
import com.guidev1911.ChatAlive.model.Group;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;
    @PostMapping
    public ResponseEntity<ApiResponse> createGroup(@RequestBody CreateGroupRequest request) {
        groupService.createGroup(
                request.getName(),
                request.getDescription(),
                request.getPrivacy()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Grupo criado com sucesso"));
    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<ApiResponse> joinGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ApiResponse response = groupService.joinGroup(userDetails.getUsername(), groupId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{groupId}/approve/{userId}")
    public ResponseEntity<ApiResponse> approveMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        groupService.approveMemberRequest(groupId, userId, userDetails.getUsername());
        return ResponseEntity.ok(new ApiResponse(true, "Solicitação aprovada."));
    }
    @DeleteMapping("/{groupId}/reject/{userId}")
    public ResponseEntity<ApiResponse> rejectMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        groupService.rejectMemberRequest(groupId, userId, userDetails.getUsername());
        return ResponseEntity.ok(new ApiResponse(true, "Solicitação rejeitada."));
    }
    @PostMapping("/{groupId}/invite/{userId}")
    public ResponseEntity<?> inviteUser(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @AuthenticationPrincipal User inviter
    ) {
        Group group = groupService.getById(groupId);
        User invitee = new User(); invitee.setId(userId);
        groupService.inviteUserToGroup(group, inviter, invitee);
        return ResponseEntity.ok("Usuário convidado.");
    }

    @DeleteMapping("/{groupId}/remove/{userId}")
    public ResponseEntity<?> removeUser(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @AuthenticationPrincipal User remover
    ) {
        groupService.removeMember(groupId, userId, remover);
        return ResponseEntity.ok("Usuário removido do grupo.");
    }


}

