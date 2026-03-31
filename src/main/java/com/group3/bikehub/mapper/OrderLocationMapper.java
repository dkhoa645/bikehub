package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.OrderLocationResponse;
import com.group3.bikehub.entity.OrderLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderLocationMapper {
    OrderLocationResponse toResponse(OrderLocation orderLocation);
}
