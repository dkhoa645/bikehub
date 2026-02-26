package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.ComponentCreationRequest;
import com.group3.bikehub.dto.response.ComponentResponse;
import com.group3.bikehub.service.InspectionComponentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/component")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionComponentController {
    InspectionComponentService inspectionComponentService;

    @PostMapping
    ApiResponse<ComponentResponse> createComponent(@RequestBody ComponentCreationRequest componentCreationRequest) {
        return ApiResponse.<ComponentResponse>builder()
                .result(inspectionComponentService.createComponent(componentCreationRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<ComponentResponse>> getComponent() {
        return ApiResponse.<List<ComponentResponse>>builder()
                .result(inspectionComponentService.getAll())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ComponentResponse> updateComponent(
            @PathVariable Long id,
            @RequestBody ComponentResponse componentResponse) {
        return ApiResponse.<ComponentResponse>builder()
                .result(inspectionComponentService.updateComponent(id,componentResponse))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteComponent(@PathVariable Long id) {
        inspectionComponentService.deleteComponent(id);
        return ApiResponse.<Void>builder()
                .message("Delete component success")
                .build();
    }

}
