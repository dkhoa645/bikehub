package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.request.ComponentCreationRequest;
import com.group3.bikehub.dto.response.ComponentResponse;
import com.group3.bikehub.entity.InspectionComponent;
import org.mapstruct.Mapper;

import java.awt.*;

@Mapper(componentModel = "spring")
public interface InspectionComponentMapper {
    InspectionComponent toEntity(ComponentCreationRequest componentCreationRequest);
    ComponentResponse toResponse(InspectionComponent component);
}
