package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.LocationCreationRequest;
import com.group3.bikehub.dto.response.InspectionLocationResponse;
import com.group3.bikehub.entity.Enum.InspectionLocationType;
import com.group3.bikehub.entity.InspectionLocation;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.InspectionLocationMapper;
import com.group3.bikehub.repository.InspectionLocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionLocationService {

    InspectionLocationRepository inspectionLocationRepository;
    InspectionLocationMapper inspectionLocationMapper;

    public InspectionLocationResponse createInspectionLocation(LocationCreationRequest locationCreationRequest) {
        InspectionLocation location = inspectionLocationMapper.toLocation(locationCreationRequest);
        location.setType(InspectionLocationType.COMPANY);
        return inspectionLocationMapper.toLocationCreationResponse(inspectionLocationRepository.save(location));
    }

    public List<InspectionLocationResponse> findAllLocation() {
        return inspectionLocationRepository.findAll().stream()
                .map(inspectionLocationMapper::toLocationCreationResponse)
                .toList();
    }

    public InspectionLocationResponse findLocation(UUID id) {
        return inspectionLocationMapper.toLocationCreationResponse(
                inspectionLocationRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.LOCATION_NOT_FOUND)));
    }

    public List<InspectionLocationResponse> findCompanyLocation() {
        return inspectionLocationRepository.findByType(InspectionLocationType.COMPANY)
                .stream()
                .map(inspectionLocationMapper::toLocationCreationResponse)
                .toList();
    }
}
