package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.InspectionCreationRequest;
import com.group3.bikehub.dto.request.LocationCreationRequest;
import com.group3.bikehub.dto.response.InspectionLocationResponse;
import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.service.InspectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/inspection")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionController {
    InspectionService inspectionService;

    @PostMapping("/my-inspection")
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
}
