package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.InspectionCreationRequest;
import com.group3.bikehub.dto.request.ListingCreationRequest;
import com.group3.bikehub.dto.response.ListingResponse;
import com.group3.bikehub.dto.response.ListingSellResponse;
import com.group3.bikehub.dto.response.PageResponse;
import com.group3.bikehub.entity.InspectionLocation;
import com.group3.bikehub.entity.ListingImage;
import com.group3.bikehub.service.ListingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.query.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/listing")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingController {
    ListingService listingService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<ListingResponse> addListing(@ModelAttribute  ListingCreationRequest listingRequest) throws IOException {
        return ApiResponse.<ListingResponse>builder()
                .result(listingService.createListing(listingRequest))
                .build();
    }

    @GetMapping()
    ApiResponse<PageResponse<ListingSellResponse>> getAllListing(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10")int size
    ){
        return ApiResponse.<PageResponse<ListingSellResponse>>builder()
                .result(listingService.getSellListing(page,size))
                .build();
    }

    @GetMapping("/my-list")
    ApiResponse<List<ListingResponse>> getListing(){
        return ApiResponse.<List<ListingResponse>>builder()
                .result(listingService.getMyListing())
                .build();
    }

    @GetMapping("/all")
    ApiResponse<List<ListingSellResponse>> getAllListings(){
        return ApiResponse.<List<ListingSellResponse>>builder()
                .result(listingService.getAllListing())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ListingResponse> getListingById(@PathVariable UUID id){
        return ApiResponse.<ListingResponse>builder()
                .result(listingService.getById(id))
                .build();
    }
}
