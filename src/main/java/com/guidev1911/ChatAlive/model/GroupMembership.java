package com.guidev1911.ChatAlive.model;

import com.guidev1911.ChatAlive.Role.GroupRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class GroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Group group;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private GroupRole role;

    private boolean pendingRequest = false;

    public GroupMembership() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GroupRole getRole() {
        return role;
    }

    public void setRole(GroupRole role) {
        this.role = role;
    }

    public boolean isPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(boolean pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMembership that = (GroupMembership) o;
        return isPendingRequest() == that.isPendingRequest() && Objects.equals(getId(), that.getId()) && Objects.equals(getGroup(), that.getGroup()) && Objects.equals(getUser(), that.getUser()) && getRole() == that.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGroup(), getUser(), getRole(), isPendingRequest());
    }
}
