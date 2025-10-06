package com.guidev1911.ChatAlive.mapper;

import com.guidev1911.ChatAlive.dto.groups.GroupDTO;
import com.guidev1911.ChatAlive.model.Group;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = GroupMembershipMapper.class)
public interface GroupMapper {
    @Mapping(source = "creator.email", target = "creatorEmail")
    @Mapping(source = "privacy", target = "privacy")
    GroupDTO toDTO(Group group);
    List<GroupDTO> toDTOList(List<Group> groups);
    @InheritInverseConfiguration
    Group toEntity(GroupDTO dto);
}