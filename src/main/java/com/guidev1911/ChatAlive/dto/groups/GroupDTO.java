package com.guidev1911.ChatAlive.dto.groups;


import java.util.List;
public class GroupDTO {
    private Long id;
    private String name;
    private String description;
    private String privacy;
    private String creatorEmail;
    private String groupImageUrl;
    private List<GroupMembershipDTO> members;

    public GroupDTO() {
    }

    public GroupDTO(Long id, String name, String description, String privacy,
                    String creatorEmail, String groupImageUrl, List<GroupMembershipDTO> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.privacy = privacy;
        this.creatorEmail = creatorEmail;
        this.groupImageUrl = groupImageUrl;
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getGroupImageUrl() {
        return groupImageUrl;
    }

    public void setGroupImageUrl(String groupImageUrl) {
        this.groupImageUrl = groupImageUrl;
    }

    public List<GroupMembershipDTO> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMembershipDTO> members) {
        this.members = members;
    }
}