package com.guidev1911.ChatAlive.mapper;

import com.guidev1911.ChatAlive.dto.groups.GroupDTO;
import com.guidev1911.ChatAlive.dto.groups.GroupMembershipDTO;
import com.guidev1911.ChatAlive.model.Group;
import com.guidev1911.ChatAlive.model.GroupMembership;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMembershipMapper {
    @Mapping(source = "user.email", target = "userEmail")
    GroupMembershipDTO toDTO(GroupMembership membership);
    List<GroupMembershipDTO> toDTOList(List<GroupMembership> memberships);
    @InheritInverseConfiguration
    GroupMembership toEntity(GroupMembershipDTO dto);
}