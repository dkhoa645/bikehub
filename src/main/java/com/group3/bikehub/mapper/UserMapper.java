package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.UserResponse;
import com.group3.bikehub.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
