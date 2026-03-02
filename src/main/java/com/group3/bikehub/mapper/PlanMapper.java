package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.request.PlanCreationRequest;
import com.group3.bikehub.dto.request.PlanUpdateRequest;
import com.group3.bikehub.dto.response.PlanResponse;
import com.group3.bikehub.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlanMapper {
    Plan toPlan(PlanCreationRequest request);
    PlanResponse toPlanResponse(Plan plan);

    void updatePlan(PlanUpdateRequest request, @MappingTarget Plan plan);
}
