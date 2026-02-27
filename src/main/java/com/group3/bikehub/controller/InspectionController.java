package com.group3.bikehub.controller;

import com.cloudinary.Api;
import com.group3.bikehub.dto.request.*;
import com.group3.bikehub.dto.response.ComponentResponse;
import com.group3.bikehub.dto.response.InspectionLocationResponse;
import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.dto.response.UserResponse;
import com.group3.bikehub.service.InspectionScoreService;
import com.group3.bikehub.service.InspectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inspection")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionController {
    InspectionService inspectionService;
    InspectionScoreService inspectionScoreService;

    @PostMapping()
    ApiResponse<InspectionResponse> createInspection(@RequestBody InspectionCreationRequest request) {
        return ApiResponse.<InspectionResponse>builder()
                .result(inspectionService.createInspection(request))
                .build();
    }

    @GetMapping("/{listingId}")
    ApiResponse<InspectionResponse> getMyInspection(@PathVariable UUID listingId) {
        return ApiResponse.<InspectionResponse>builder()
                .result(inspectionService.getInspection(listingId))
                .build();
    }

    @GetMapping()
    ApiResponse<List<InspectionResponse>> getInspections() {
        return ApiResponse.<List<InspectionResponse>>builder()
                .result(inspectionService.getAll())
                .build();
    }

    @GetMapping("/my-assign")
    ApiResponse<List<InspectionResponse>> getMyAssignedInspections() {
        return ApiResponse.<List<InspectionResponse>>builder()
                .result(inspectionService.getMyAssign())
                .build();
    }

    @PutMapping("/assign-inspector")
    ApiResponse<InspectionResponse> updateInspection(@RequestBody InspectionAssignRequest inspectionAssignRequest) {
        return ApiResponse.<InspectionResponse>builder()
                .result(inspectionService.assignInspector(inspectionAssignRequest))
                .build();
    }

    @GetMapping("/inspector")
    ApiResponse<List<UserResponse>> getInspectorByTime(@RequestBody )

    @PostMapping("/{inspectionId}/scores")
    ApiResponse<InspectionResponse> getComponentScores(
            @PathVariable UUID inspectionId,
            @RequestBody List<ScoreCreationRequest> scoreCreationRequestList) {
        return ApiResponse.<InspectionResponse>builder()
                .result(inspectionScoreService.createScore(inspectionId,scoreCreationRequestList))
                .build();
    }

    @GetMapping("/pending")
    ApiResponse<List<InspectionResponse>> getPendingInspections() {
        return ApiResponse.<List<InspectionResponse>>builder()
                .result(inspectionService.getPending())
                .build();
    }

}
