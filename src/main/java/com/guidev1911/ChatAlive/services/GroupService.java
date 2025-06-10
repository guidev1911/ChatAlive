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

    public void approveMemberRequest(Long groupId, Long userIdToApprove, User approver) {
        Group group = getById(groupId);

        GroupMembership approverMembership = membershipRepository.findByGroupAndUser(group, approver)
                .orElseThrow(() -> new RuntimeException("Ação não permitida"));

        if (approverMembership.getRole() == GroupRole.MEMBER)
            throw new RuntimeException("Apenas criador ou administrador podem aprovar membros.");

        GroupMembership pending = membershipRepository.findByGroupAndUser(group, new User() {{ setId(userIdToApprove); }})
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        if (!pending.isPendingRequest())
            throw new RuntimeException("Este usuário já é membro ou foi aprovado.");

        pending.setPendingRequest(false);
        membershipRepository.save(pending);
    }

    public void inviteUserToGroup(Group group, User inviter, User invitee) {
        GroupMembership inviterMembership = membershipRepository.findByGroupAndUser(group, inviter)
                .orElseThrow(() -> new RuntimeException("Você não pertence a esse grupo"));

        if (inviterMembership.getRole() == GroupRole.MEMBER)
            throw new RuntimeException("Apenas criador ou administrador podem convidar.");

        boolean alreadyMember = membershipRepository.findByGroupAndUser(group, invitee).isPresent();
        if (alreadyMember)
            throw new RuntimeException("Usuário já está no grupo.");

        GroupMembership membership = new GroupMembership();
        membership.setGroup(group);
        membership.setUser(invitee);
        membership.setRole(GroupRole.MEMBER);
        membership.setPendingRequest(false);

        membershipRepository.save(membership);
    }

    public void removeMember(Long groupId, Long targetUserId, User currentUser) {
        Group group = getById(groupId);

        GroupMembership currentMembership = membershipRepository.findByGroupAndUser(group, currentUser)
                .orElseThrow(() -> new RuntimeException("Você não está no grupo"));

        GroupMembership targetMembership = membershipRepository.findByGroupAndUser(group, new User() {{ setId(targetUserId); }})
                .orElseThrow(() -> new RuntimeException("Usuário alvo não está no grupo"));

        if (!canRemove(currentUser, targetMembership)) {
            throw new RuntimeException("Você não tem permissão para remover este membro.");
        }

        membershipRepository.delete(targetMembership);
    }


}
