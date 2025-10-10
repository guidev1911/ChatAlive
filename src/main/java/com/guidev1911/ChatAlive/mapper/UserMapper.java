package com.guidev1911.ChatAlive.mapper;

import com.guidev1911.ChatAlive.dto.users.UserDTO;
import com.guidev1911.ChatAlive.model.PendingUser;
import com.guidev1911.ChatAlive.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encodedPassword", ignore = true)
    @Mapping(target = "confirmationCode", ignore = true)
    @Mapping(target = "expirationTime", ignore = true)
    PendingUser toPendingUser(UserDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    User toUser(PendingUser pendingUser);
}
