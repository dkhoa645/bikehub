package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.*;
import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.dto.response.PageResponse;
import com.group3.bikehub.dto.response.UserResponse;
import com.group3.bikehub.service.InspectionScoreService;
import com.group3.bikehub.service.InspectionService;
import com.group3.bikehub.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @GetMapping("/page")
    ApiResponse<PageResponse<InspectionResponse>> getPageInspections(
            @RequestParam(value = "page", required = false, defaultValue = "1")int page,
            @RequestParam(value = "size", required = false, defaultValue = "10")int size
    ) {
        return ApiResponse.<PageResponse<InspectionResponse>>builder()
                .result(inspectionService.getPageInspection(page,size))
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

    @GetMapping("/available-inspector")
    ApiResponse<List<UserResponse>> getInspectorByTime(
            @Parameter(
                    description = "Time to search available inspector",
                    example = "2026-02-27T08:03:08.206Z"
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Date scheduleAt) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(inspectionService.getAvailableInspector(scheduleAt))
                .build();
    }

    @PostMapping(value = "/{inspectionId}/scores", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<InspectionResponse> getComponentScores(
            @PathVariable UUID inspectionId,
            @ModelAttribute ScoreCreationRequest scoreCreationRequestList) {
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

    @GetMapping("/my-inspection")
    ApiResponse<List<InspectionResponse>> getMyInspections() {
        return ApiResponse.<List<InspectionResponse>>builder()
                .result(inspectionService.getMyInspection())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<InspectionResponse> getInspectionById(@RequestParam UUID id) {
        return ApiResponse.<InspectionResponse>builder()
                .result(inspectionService.getInspectionById(id))
                .build();
    }

}
