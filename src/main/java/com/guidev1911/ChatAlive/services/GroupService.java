package com.guidev1911.ChatAlive.services;

import com.guidev1911.ChatAlive.Role.GroupPrivacy;
import com.guidev1911.ChatAlive.Role.GroupRole;
import com.guidev1911.ChatAlive.model.Group;
import com.guidev1911.ChatAlive.model.GroupMembership;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.repository.GroupMembershipRepository;
import com.guidev1911.ChatAlive.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired private GroupMembershipRepository membershipRepository;

    public Group createGroup(String name, GroupPrivacy privacy, User creator) {
        Group group = new Group();
        group.setName(name);
        group.setPrivacy(privacy);
        group.setCreator(creator);

        group = groupRepository.save(group);

        GroupMembership membership = new GroupMembership();
        membership.setGroup(group);
        membership.setUser(creator);
        membership.setRole(GroupRole.CREATOR);
        membership.setPendingRequest(false);

        membershipRepository.save(membership);
        return group;
    }

    public void joinGroup(User user, Group group) {
        if (group.getPrivacy() == GroupPrivacy.PUBLIC) {
            GroupMembership membership = new GroupMembership();
            membership.setGroup(group);
            membership.setUser(user);
            membership.setRole(GroupRole.MEMBER);
            membership.setPendingRequest(false);
            membershipRepository.save(membership);
        } else if (group.getPrivacy() == GroupPrivacy.PRIVATE) {
            GroupMembership membership = new GroupMembership();
            membership.setGroup(group);
            membership.setUser(user);
            membership.setRole(GroupRole.MEMBER);
            membership.setPendingRequest(true);
            membershipRepository.save(membership);
        } else {
            throw new IllegalStateException("Este grupo só permite entrada por convite.");
        }
    }

    public boolean canRemove(User currentUser, GroupMembership targetMembership) {
        GroupMembership currentMembership = membershipRepository.findByGroupAndUser(
                targetMembership.getGroup(), currentUser
        ).orElse(null);

        if (currentMembership == null) return false;

        switch (currentMembership.getRole()) {
            case CREATOR:
                return true;
            case ADMIN:
                return targetMembership.getRole() == GroupRole.MEMBER;
            default:
                return false;
        }
    }
    public Group getById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado com ID: " + groupId));
    }

}
