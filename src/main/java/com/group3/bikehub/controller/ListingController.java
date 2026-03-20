package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.InspectionCreationRequest;
import com.group3.bikehub.dto.request.ListingCreationRequest;
import com.group3.bikehub.dto.request.ListingUpdateRequest;
import com.group3.bikehub.dto.response.ListingResponse;
import com.group3.bikehub.dto.response.ListingSellResponse;
import com.group3.bikehub.dto.response.PageResponse;
import com.group3.bikehub.entity.InspectionLocation;
import com.group3.bikehub.entity.ListingImage;
import com.group3.bikehub.service.ListingService;
import jakarta.validation.Valid;
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
    ApiResponse<ListingResponse> addListing(@ModelAttribute @Valid ListingCreationRequest listingRequest) throws IOException {
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

    @GetMapping("/seller/{listingId}")
    ApiResponse<ListingResponse> getSellerListingById(@PathVariable UUID listingId){
        return ApiResponse.<ListingResponse>builder()
                .result(listingService.getBySellerId(listingId))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ListingSellResponse> getListingById(@PathVariable UUID id){
        return ApiResponse.<ListingSellResponse>builder()
                .result(listingService.getById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteListingById(@PathVariable UUID id){
        listingService.deleteListing(id);
        return ApiResponse.<Void>builder()
                .message("Deleted Successfully!")
                .build();
    }


    @PutMapping(value =  "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<ListingResponse> updateListing(
            @PathVariable UUID id,
            @ModelAttribute @Valid ListingUpdateRequest request){
        return ApiResponse.<ListingResponse>builder()
                .result(listingService.updateListing(id,request))
                .build();
    }
}
