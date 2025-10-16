package com.guidev1911.ChatAlive.controllers;

import com.guidev1911.ChatAlive.Role.GroupPrivacy;
import com.guidev1911.ChatAlive.dto.groups.GroupDTO;
import com.guidev1911.ChatAlive.dto.responses.ApiResponse;
import com.guidev1911.ChatAlive.dto.groups.CreateGroupRequest;
import com.guidev1911.ChatAlive.model.Group;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<Page<Group>> getAllGroups(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Group> groups = groupService.getAllGroups(name, pageable);

        return ResponseEntity.ok(groups);
    }
    @GetMapping("/my-groups")
    public ResponseEntity<Page<GroupDTO>> getUserGroups(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<GroupDTO> groups = groupService.getGroupsForAuthenticatedUser(pageable);
        return ResponseEntity.ok(groups);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createGroup(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam GroupPrivacy privacy,
            @RequestParam(required = false) MultipartFile photoFile) {

        groupService.createGroup(name, description, privacy, photoFile);

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

