package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.InspectionImageResponse;
import com.group3.bikehub.entity.InspectionImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InspectionImageMapper {
    InspectionImageResponse toResponse(InspectionImage inspectionImage);
}
