package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.request.PlanCreationRequest;
import com.group3.bikehub.dto.request.PlanUpdateRequest;
import com.group3.bikehub.dto.response.PlanResponse;
import com.group3.bikehub.entity.Plan;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PlanMapper {
    Plan toPlan(PlanCreationRequest request);
    PlanResponse toPlanResponse(Plan plan);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePlan(PlanUpdateRequest request, @MappingTarget Plan plan);
}
