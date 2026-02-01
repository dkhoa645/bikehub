package com.group3.bikehub.controller;


import com.group3.bikehub.dto.request.*;
import com.group3.bikehub.dto.response.KycDraftResponse;
import com.group3.bikehub.entity.Kyc;
import com.group3.bikehub.service.KycService;
import com.group3.bikehub.service.impl.KycDraftStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
    @RequestMapping("/kyc")
    public class KycController {
    @Autowired
    KycService kycService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<KycDraftResponse> upload(
            @ModelAttribute KycUploadRequest request) {
        return ApiResponse.<KycDraftResponse>builder().result(kycService.upload(request.getImage())).build();
    }

    @PostMapping("/confirm")
    public ApiResponse<Void> confirm(@RequestBody KycConfirmRequest request, @AuthenticationPrincipal Jwt jwt) {
        kycService.confirmKyc(jwt.getSubject(), request.getDraftId());
        return ApiResponse.<Void>builder()
                .message("KYC CONFIRMED")
                .build();
    }

    @GetMapping("/getall")
    public ApiResponse<List<Kyc>> getAllKyc(){
        return ApiResponse.<List<Kyc>>builder().result(kycService.getAllKyc()).build();
    }
    @PostMapping("/delete")
    public ApiResponse<Void> deleteKycById(@RequestBody KycDeleteRequest request){
        kycService.deleteKycById(request.getId());
        return ApiResponse.<Void>builder().message("success").build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/verify")
    public ApiResponse<Void> verifyKyc(@RequestBody KycVerifyRequest request) {
        kycService.verifyKyc(request.getId(), request.getApproved());
        return ApiResponse.<Void>builder()
                .message("KYC VERIFIED SUCCESSFULLY")
                .build();
    }

}




