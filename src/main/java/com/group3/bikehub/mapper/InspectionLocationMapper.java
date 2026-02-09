package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.request.LocationCreationRequest;
import com.group3.bikehub.dto.request.LocationUpdateRequest;
import com.group3.bikehub.dto.response.InspectionLocationResponse;
import com.group3.bikehub.entity.Address;
import com.group3.bikehub.entity.InspectionLocation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InspectionLocationMapper {
    InspectionLocationResponse toLocationCreationResponse(InspectionLocation inspectionLocation);
    InspectionLocation toLocation(LocationCreationRequest locationCreationRequest);
    InspectionLocation fromAddress (Address address);
    void updateLocation(LocationUpdateRequest request, @MappingTarget InspectionLocation inspectionLocation);

}
