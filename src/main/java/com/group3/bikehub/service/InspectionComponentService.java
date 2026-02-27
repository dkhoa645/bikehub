package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.ComponentCreationRequest;
import com.group3.bikehub.dto.response.ComponentResponse;
import com.group3.bikehub.entity.InspectionComponent;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.InspectionComponentMapper;
import com.group3.bikehub.repository.InspectionComponentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionComponentService {
    InspectionComponentRepository inspectionComponentRepository;
    InspectionComponentMapper inspectionComponentMapper;

    public ComponentResponse createComponent(ComponentCreationRequest componentCreationRequest) {
        InspectionComponent component = inspectionComponentMapper.toEntity(componentCreationRequest);
        if(inspectionComponentRepository.existsByName(component.getName())) {
            throw new AppException(ErrorCode.COMPONENT_EXISTS);
        }
        return inspectionComponentMapper.toResponse(inspectionComponentRepository.save(component));
    }

    public List<ComponentResponse> getAll() {
        return inspectionComponentRepository.findAll().stream()
                .map(inspectionComponentMapper::toResponse)
                .toList();
    }

    public ComponentResponse updateComponent(Long id, ComponentResponse componentResponse) {
        InspectionComponent inspectionComponent = inspectionComponentRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.INSPECTION_NOT_FOUND));
        inspectionComponent.setName(componentResponse.getName());
        return inspectionComponentMapper.toResponse(inspectionComponentRepository.save(inspectionComponent));
    }


    public void deleteComponent(Long id) {
        InspectionComponent component = inspectionComponentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INSPECTION_NOT_FOUND));

        inspectionComponentRepository.delete(component);
    }
}
