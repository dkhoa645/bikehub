package com.group3.bikehub.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InspectionScoreMapper {
    ScoreResponse toScoreResponse(InspectionScore inspectionScore);
}
