package com.group3.bikehub.controller;

import com.cloudinary.Api;
import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.BrandCreationRequest;
import com.group3.bikehub.dto.request.BrandUpdateRequest;
import com.group3.bikehub.dto.response.BrandResponse;
import com.group3.bikehub.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {

    BrandService brandService;

    @GetMapping()
    ApiResponse<List<BrandResponse>> getAllBrands() {
        return ApiResponse.<List<BrandResponse>>builder()
                .result(brandService.getAllBrand())
                .build();
    }

    @PostMapping()
    ApiResponse<BrandResponse> addBrands(@RequestBody BrandCreationRequest request){
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.addBrand(request))
                .build();
    }

    @PutMapping()
    ApiResponse<BrandResponse> updateBrands(@RequestBody BrandUpdateRequest request){
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.updateBrand(request))
                .build();
    }
}
