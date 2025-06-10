package com.guidev1911.ChatAlive.dto;

import com.guidev1911.ChatAlive.Role.GroupPrivacy;

public class CreateGroupRequest {
    private String name;
    private GroupPrivacy privacy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(GroupPrivacy privacy) {
        this.privacy = privacy;
    }
}
