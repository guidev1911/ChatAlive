package com.guidev1911.ChatAlive.controllers;

import com.guidev1911.ChatAlive.dto.CreateGroupRequest;
import com.guidev1911.ChatAlive.model.Group;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroupRequest request, @AuthenticationPrincipal User user) {
        Group group = groupService.createGroup(request.getName(), request.getPrivacy(), user);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<String> joinGroup(@PathVariable Long groupId, @AuthenticationPrincipal User user) {
        Group group = groupService.getById(groupId);
        groupService.joinGroup(user, group);
        return ResponseEntity.ok("Solicitação enviada ou entrada efetuada.");
    }

}

