package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.SubscriptionCreationRequest;
import com.group3.bikehub.dto.response.SubscriptionResponse;
import com.group3.bikehub.service.SubscriptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionController {
    SubscriptionService subscriptionService;

    @PostMapping()
    ApiResponse<SubscriptionResponse> createSubscription(
            @RequestBody SubscriptionCreationRequest subscriptionCreationRequest){
        return ApiResponse.<SubscriptionResponse>builder()
                .result(subscriptionService.createSubscription(subscriptionCreationRequest))
                .build();
    }

    @GetMapping("/{listingId}")
    ApiResponse<List<SubscriptionResponse>> getSubscription(@PathVariable UUID listingId){
        return ApiResponse.<List<SubscriptionResponse>>builder()
                .result(subscriptionService.getSubscriptionByListing(listingId))
                .build();
    }

    @PutMapping("/expired")
    ApiResponse<Void> updateSubscription(){
        subscriptionService.expiredSubs();
        return ApiResponse.<Void>builder()
                .message("Expired Subscription successful")
                .build();
    }

}
