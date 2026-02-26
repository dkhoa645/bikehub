package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.ScoreResponse;
import com.group3.bikehub.entity.InspectionScore;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InspectionScoreMapper {
    ScoreResponse toScoreResponse(InspectionScore inspectionScore);
}
