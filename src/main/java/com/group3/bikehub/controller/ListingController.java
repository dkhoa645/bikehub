package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.InspectionCreationRequest;
import com.group3.bikehub.dto.request.ListingCreationRequest;
import com.group3.bikehub.dto.response.ListingResponse;
import com.group3.bikehub.entity.InspectionLocation;
import com.group3.bikehub.entity.ListingImage;
import com.group3.bikehub.service.ListingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listing")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingController {
    ListingService listingService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<ListingResponse> addListing(@ModelAttribute ListingCreationRequest listingRequest){
        return ApiResponse.<ListingResponse>builder()
                .result(listingService.createListing(listingRequest))
                .build();
    }
}
