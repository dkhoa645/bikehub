package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.PlanCreationRequest;
import com.group3.bikehub.dto.request.PlanUpdateRequest;
import com.group3.bikehub.dto.response.PlanResponse;
import com.group3.bikehub.entity.Plan;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.PlanMapper;
import com.group3.bikehub.repository.PlanRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlanService {
    PlanRepository planRepository;
    PlanMapper planMapper;

    public PlanResponse createPlan(PlanCreationRequest request) {
        Plan plan = planMapper.toPlan(request);
        if(planRepository.existsByNameAndDurationDays(request.getName(), request.getDurationDays()))
            throw new AppException(ErrorCode.PLAN_EXISTS);
        return planMapper.toPlanResponse(planRepository.save(plan));
    }

    public List<PlanResponse> getAll() {
        return planRepository.findAll().stream()
                .map(planMapper::toPlanResponse)
                .toList();
    }

    public void deleteList(Long id) {
        if(planRepository.existsById(id)) {
            planRepository.deleteById(id);
        }else{
            throw new AppException(ErrorCode.PLAN_NOT_FOUND);
        }
    }

    public PlanResponse updateList(Long id, PlanUpdateRequest request) {
        Plan plan = planRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));
        planMapper.updatePlan(request,plan);
        return planMapper.toPlanResponse(planRepository.save(plan));
    }
}
