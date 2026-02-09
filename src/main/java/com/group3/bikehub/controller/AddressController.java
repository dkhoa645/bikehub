package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.AddressCreationRequest;
import com.group3.bikehub.dto.request.AddressUpdateRequest;
import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.BrandCreationRequest;
import com.group3.bikehub.dto.response.AddressResponse;
import com.group3.bikehub.dto.response.BrandResponse;
import com.group3.bikehub.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressService addressService;

    @PostMapping()
    ApiResponse<AddressResponse> addAddress(@RequestBody AddressCreationRequest request){
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.create(request))
                .build();
    }

    @GetMapping("/my-address")
    ApiResponse<AddressResponse> getMyAddress(){
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.findMyAddress())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<AddressResponse> changeMyAddress(
            @RequestBody AddressUpdateRequest request,
            @PathVariable UUID id){
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.updateMyAddress(request,id))
                .build();
    }
}
