package com.group3.bikehub.controller;


import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.response.KycDraftResponse;
import com.group3.bikehub.dto.response.KycResponse;
import com.group3.bikehub.mapper.KycMapper;
import com.group3.bikehub.service.KycService;
import com.group3.bikehub.service.impl.KycDraftStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
    @RequestMapping("/kyc")
    public class KycController {
    @Autowired
    KycService kycService;
    @Autowired
    KycDraftStoreService kycDraftStoreService;
    @Autowired
    KycMapper kycMapper;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<KycDraftResponse> upload(
            @RequestParam("image") MultipartFile image) {
         KycResponse kycResponse =  kycService.ocr(image);

        String draftId = kycDraftStoreService.save(kycResponse);

        KycDraftResponse response = new KycDraftResponse(
                draftId,
                kycResponse
        );
        return ApiResponse.<KycDraftResponse>builder().result(response).build();
    }

    @PostMapping("/confirm/{draftId}")
    public ApiResponse<Void> confirm(@PathVariable String draftId) {
        kycService.save(draftId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("KYC CONFIRMED")
                .build();
    }


}




