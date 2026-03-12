package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.OrderResponse;
import com.group3.bikehub.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse (Order order);
}
