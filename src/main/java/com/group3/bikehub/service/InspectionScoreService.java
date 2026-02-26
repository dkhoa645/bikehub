package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.ScoreCreationRequest;
import com.group3.bikehub.dto.response.ComponentResponse;
import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.entity.Inspection;
import com.group3.bikehub.entity.InspectionComponent;
import com.group3.bikehub.entity.InspectionScore;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.InspectionMapper;
import com.group3.bikehub.mapper.InspectionScoreMapper;
import com.group3.bikehub.repository.InspectionComponentRepository;
import com.group3.bikehub.repository.InspectionRepository;
import com.group3.bikehub.repository.InspectionScoreRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionScoreService {
    InspectionScoreRepository inspectionScoreRepository;
    InspectionRepository inspectionRepository;
    InspectionComponentRepository inspectionComponentRepository;
    InspectionScoreMapper inspectionScoreMapper;
    InspectionMapper inspectionMapper;

    public InspectionResponse createScore(UUID inspectionId, List<ScoreCreationRequest> scoreCreationRequestList) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new AppException(ErrorCode.INSPECTION_NOT_FOUND));
        if(!inspection.getScores().isEmpty()){
            throw new AppException(ErrorCode.COMPONENT_EXISTS);
        }
        List<InspectionScore> scores = scoreCreationRequestList.stream()
                .map(request -> {
                    InspectionComponent inspectionComponent = inspectionComponentRepository
                            .findById(request.getComponentId())
                            .orElseThrow(()->new AppException(ErrorCode.COMPONENT_NOT_FOUND));
                    return inspectionScoreRepository.save(InspectionScore.builder()
                            .score(request.getScore())
                            .inspection(inspection)
                            .component(inspectionComponent)
                            .build());
                }).toList();

        InspectionResponse inspectionResponse = inspectionMapper.toInspectionResponse(inspection);

        inspectionResponse.setScores(
                scores.stream().map(inspectionScoreMapper::toScoreResponse).toList()
        );

        return inspectionResponse;
    }
}
