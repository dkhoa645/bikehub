package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.LocationCreationRequest;
import com.group3.bikehub.dto.request.LocationUpdateRequest;
import com.group3.bikehub.dto.response.InspectionLocationResponse;

import com.group3.bikehub.service.InspectionLocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionLocationController {

    InspectionLocationService inspectionLocationService;

    @PostMapping()
    ApiResponse<InspectionLocationResponse> createLocation(@RequestBody LocationCreationRequest locationCreationRequest) {
        return ApiResponse.<InspectionLocationResponse>builder()
                .result(inspectionLocationService.createInspectionLocation(locationCreationRequest))
                .build();
    }

    @GetMapping()
    ApiResponse<List<InspectionLocationResponse>> getLocation() {
        return ApiResponse.<List<InspectionLocationResponse>>builder()
                .result(inspectionLocationService.findAllLocation())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<InspectionLocationResponse> getLocation(@PathVariable UUID id) {
        return ApiResponse.<InspectionLocationResponse>builder()
                .result(inspectionLocationService.findLocation(id))
                .build();
    }

    @GetMapping("/company")
    ApiResponse<List<InspectionLocationResponse>> getCompanyLocation() {
        return ApiResponse.<List<InspectionLocationResponse>>builder()
                .result(inspectionLocationService.findCompanyLocation())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<InspectionLocationResponse> updateLocation(
            @PathVariable UUID id,
            @RequestBody LocationUpdateRequest request
    ) {
        return ApiResponse.<InspectionLocationResponse>builder()
                .result(inspectionLocationService.updateLocation(request,id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteLocation(@PathVariable UUID id) {
        inspectionLocationService.deleteLocation(id);
        return ApiResponse.<Void>builder()
                .message("Location has been deleted")
                .build();
    }

}
