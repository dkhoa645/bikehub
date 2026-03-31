package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.PlanCreationRequest;
import com.group3.bikehub.dto.request.PlanUpdateRequest;
import com.group3.bikehub.dto.response.PlanResponse;
import com.group3.bikehub.service.PlanService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlanController {
    PlanService planService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PlanResponse> createPLan(@RequestBody @Valid PlanCreationRequest request){
        return ApiResponse.<PlanResponse>builder()
                .result(planService.createPlan(request))
                .build();
    }


    @GetMapping()
    ApiResponse<List<PlanResponse>> getAllLists(){
        return ApiResponse.<List<PlanResponse>>builder()
                .result(planService.getAll())
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteList(@PathVariable Long id){
        planService.deleteList(id);
        return ApiResponse.<Void>builder()
                .message("Successfully deleted list")
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PlanResponse> updatePLan(
            @PathVariable Long id,
            @RequestBody @Valid PlanUpdateRequest request){
        return ApiResponse.<PlanResponse>builder()
                .result(planService.updateList(id,request))
                .build();
    }
}
