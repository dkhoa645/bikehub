package com.group3.bikehub.controller;


import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.KycConfirmRequest;
import com.group3.bikehub.dto.request.KycUploadRequest;
import com.group3.bikehub.dto.request.VerifyKycRequest;
import com.group3.bikehub.dto.response.KycDraftResponse;
import com.group3.bikehub.dto.response.KycResponse;
import com.group3.bikehub.service.KycService;
import com.group3.bikehub.service.impl.KycDraftStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
    @RequestMapping("/kyc")
    public class KycController {
    @Autowired
    KycService kycService;
    @Autowired
    KycDraftStoreService kycDraftStoreService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<KycDraftResponse> upload(
            @ModelAttribute KycUploadRequest request) {
        return ApiResponse.<KycDraftResponse>builder().result(kycService.ocr(request.getImage())).build();
    }

    @PostMapping("/confirm")
    public ApiResponse<Void> confirm(@RequestBody KycConfirmRequest request, @AuthenticationPrincipal Jwt jwt) {
        kycService.confirmKyc(jwt.getSubject(), request.getDraftId());
        return ApiResponse.<Void>builder()
                .message("KYC CONFIRMED")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/verify")
    public ApiResponse<Void> verifyKyc(@RequestBody VerifyKycRequest request) {
        kycService.verifyKyc(request.getIdNumber(), request.getApproved());
        return ApiResponse.<Void>builder()
                .message("KYC VERIFIED SUCCESSFULLY")
                .build();
    }

}




