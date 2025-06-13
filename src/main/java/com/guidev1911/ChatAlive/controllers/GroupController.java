package com.guidev1911.ChatAlive.controllers;

import com.guidev1911.ChatAlive.dto.CreateGroupRequest;
import com.guidev1911.ChatAlive.model.Group;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroupRequest request) {
        Group group = groupService.createGroup(request.getName(), request.getPrivacy());
        return ResponseEntity.ok(group);
    }
    @PostMapping("/{groupId}/join")
    public ResponseEntity<String> joinGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        groupService.joinGroup(userDetails.getUsername(), groupId);
        return ResponseEntity.ok("Solicitação enviada ou entrada efetuada.");
    }
    @PostMapping("/{groupId}/approve/{userId}")
    public ResponseEntity<?> approveMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @AuthenticationPrincipal User approver
    ) {
        groupService.approveMemberRequest(groupId, userId, approver);
        return ResponseEntity.ok("Solicitação aprovada.");
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

