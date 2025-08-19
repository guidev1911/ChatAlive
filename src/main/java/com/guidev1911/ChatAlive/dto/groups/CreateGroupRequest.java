package com.guidev1911.ChatAlive.dto.groups;

import com.guidev1911.ChatAlive.Role.GroupPrivacy;

public class CreateGroupRequest {
    private String name;
    private String description;
    private GroupPrivacy privacy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroupPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(GroupPrivacy privacy) {
        this.privacy = privacy;
    }
}
