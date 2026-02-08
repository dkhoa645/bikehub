package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.request.InspectionCreationRequest;
import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.entity.Inspection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InspectionMapper {

    Inspection toInspection (InspectionCreationRequest inspectionCreationRequest);
    InspectionResponse toInspectionResponse (Inspection inspection);
}
