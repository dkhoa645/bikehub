package com.group3.bikehub.controller;


import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.KycConfirmRequest;
import com.group3.bikehub.dto.request.KycUploadRequest;
import com.group3.bikehub.dto.response.KycDraftResponse;
import com.group3.bikehub.dto.response.KycResponse;
import com.group3.bikehub.service.KycService;
import com.group3.bikehub.service.impl.KycDraftStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
         KycResponse kycResponse =  kycService.ocr(request.getImage());

        String draftId = kycDraftStoreService.save(kycResponse);

        KycDraftResponse response = new KycDraftResponse(
                draftId,
                kycResponse
        );
        return ApiResponse.<KycDraftResponse>builder().result(response).build();
    }

    @PostMapping("/confirm")
    public ApiResponse<Void> confirm(@RequestBody KycConfirmRequest request) {
        kycService.save(request.getDraftId());
        return ApiResponse.<Void>builder()
                .message("KYC CONFIRMED")
                .build();
    }


}




