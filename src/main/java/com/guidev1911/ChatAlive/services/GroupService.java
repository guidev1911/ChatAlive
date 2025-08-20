package com.guidev1911.ChatAlive.services;

import com.guidev1911.ChatAlive.Role.GroupPrivacy;
import com.guidev1911.ChatAlive.Role.GroupRole;
import com.guidev1911.ChatAlive.dto.responses.ApiResponse;
import com.guidev1911.ChatAlive.exception.customizedExceptions.groupExceptions.GroupAccessException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.groupExceptions.GroupAlreadyExistsException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.groupExceptions.GroupNotFoundException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.groupExceptions.GroupRequestException;
import com.guidev1911.ChatAlive.exception.customizedExceptions.userExceptions.UserNotFoundException;
import com.guidev1911.ChatAlive.model.Group;
import com.guidev1911.ChatAlive.model.GroupMembership;
import com.guidev1911.ChatAlive.model.User;
import com.guidev1911.ChatAlive.repository.GroupMembershipRepository;
import com.guidev1911.ChatAlive.repository.GroupRepository;
import com.guidev1911.ChatAlive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMembershipRepository membershipRepository;

    public GroupService(UserRepository userRepository,
                        GroupRepository groupRepository,
                        GroupMembershipRepository membershipRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
    }

    public Page<Group> getAllGroups(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    public Group createGroup(String name, String description, GroupPrivacy privacy) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = VerificationByEmail(email);

        if (groupRepository.findByName(name).isPresent()) {
            throw new GroupAlreadyExistsException("Já existe um grupo com este nome.");
        }

        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
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

    public ApiResponse joinGroup(String email, Long groupId) {
        User user = VerificationByEmail(email);
        Group group = getById(groupId);

        if (membershipRepository.findByGroupAndUser(group, user).isPresent()) {
            throw new GroupRequestException("Você já é membro ou já enviou solicitação para este grupo.");
        }

        GroupMembership membership = new GroupMembership();
        membership.setGroup(group);
        membership.setUser(user);
        membership.setRole(GroupRole.MEMBER);

        if (group.getPrivacy() == GroupPrivacy.PUBLIC) {
            membership.setPendingRequest(false);
            membershipRepository.save(membership);
            return new ApiResponse(true, "Você entrou no grupo com sucesso.");
        } else if (group.getPrivacy() == GroupPrivacy.PRIVATE) {
            membership.setPendingRequest(true);
            membershipRepository.save(membership);
            return new ApiResponse(true, "Solicitação de entrada enviada com sucesso.");
        } else {
            throw new GroupAccessException("Este grupo só permite entrada por convite.");
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

    public void approveMemberRequest(Long groupId, Long userIdToApprove, String approverEmail) {
        Group group = getById(groupId);
        User approver = VerificationByEmail(approverEmail);

        if (group.getPrivacy() == GroupPrivacy.CLOSED) {
            throw new GroupAccessException("Este grupo é fechado. Só é possível entrar por convite de um administrador.");
        }
        GroupMembership approverMembership = membershipRepository.findByGroupAndUser(group, approver)
                .orElseThrow(() -> new GroupAccessException("Você não tem permissão para aprovar membros."));
        if (approverMembership.getRole() == GroupRole.MEMBER) {
            throw new GroupAccessException("Apenas criador ou administrador podem aprovar membros.");
        }
        User userToApprove = userRepository.findById(userIdToApprove)
                .orElseThrow(() -> new UserNotFoundException("Usuário a ser aprovado não encontrado."));

        GroupMembership pending = membershipRepository.findByGroupAndUser(group, userToApprove)
                .orElseThrow(() -> new GroupRequestException("Solicitação de entrada não encontrada."));

        if (!pending.isPendingRequest()) {
            throw new GroupRequestException("Este usuário já é membro ou sua solicitação já foi aprovada.");
        }
        pending.setPendingRequest(false);
        membershipRepository.save(pending);
    }
    public void rejectMemberRequest(Long groupId, Long userIdToReject, String approverEmail) {
        Group group = getById(groupId);
        User approver = VerificationByEmail(approverEmail);

        GroupMembership approverMembership = membershipRepository.findByGroupAndUser(group, approver)
                .orElseThrow(() -> new GroupAccessException("Você não tem permissão para rejeitar membros."));

        if (approverMembership.getRole() == GroupRole.MEMBER) {
            throw new GroupAccessException("Apenas criador ou administrador podem rejeitar membros.");
        }

        User userToReject = userRepository.findById(userIdToReject)
                .orElseThrow(() -> new UserNotFoundException("Usuário a ser rejeitado não encontrado."));

        GroupMembership pending = membershipRepository.findByGroupAndUser(group, userToReject)
                .orElseThrow(() -> new GroupRequestException("Solicitação de entrada não encontrada."));

        if (!pending.isPendingRequest()) {
            throw new GroupRequestException("Este usuário já é membro ou sua solicitação já foi processada.");
        }

        membershipRepository.delete(pending);
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

    public Group getById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Grupo não encontrado com ID: " + groupId));
    }

    private User VerificationByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o email: " + email));
    }

}
