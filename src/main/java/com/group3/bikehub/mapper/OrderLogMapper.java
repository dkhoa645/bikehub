package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.OrderLogResponse;
import com.group3.bikehub.entity.OrderLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderLogMapper {
    OrderLogResponse toResponse(OrderLog orderLog);
}
