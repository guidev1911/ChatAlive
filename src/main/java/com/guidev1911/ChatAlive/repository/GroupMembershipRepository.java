package com.guidev1911.ChatAlive.repository;

import com.guidev1911.ChatAlive.model.Group;
import com.guidev1911.ChatAlive.model.GroupMembership;
import com.guidev1911.ChatAlive.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {
    Optional<GroupMembership> findByGroupAndUser(Group group, User user);
    List<GroupMembership> findByGroup(Group group);
}
