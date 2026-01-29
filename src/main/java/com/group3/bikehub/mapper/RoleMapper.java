package com.group3.bikehub.mapper;


import com.group3.bikehub.dto.response.RoleResponse;
import com.group3.bikehub.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
}
