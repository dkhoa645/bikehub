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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inspection")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionController {
    InspectionService inspectionService;

    @PostMapping()
    ApiResponse<InspectionResponse> createInspection(@RequestBody InspectionCreationRequest request) {
        return ApiResponse.<InspectionResponse>builder()
                .result(inspectionService.createInspection(request))
                .build();
    }
}
