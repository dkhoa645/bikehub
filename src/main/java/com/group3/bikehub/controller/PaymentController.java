package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.OrderCreationRequest;
import com.group3.bikehub.dto.request.PaymentCreationRequest;
import com.group3.bikehub.dto.request.PaymentFilterRequest;
import com.group3.bikehub.dto.response.PageResponse;
import com.group3.bikehub.dto.response.PaymentCreationResponse;
import com.group3.bikehub.dto.response.PaymentResponse;
import com.group3.bikehub.entity.Enum.PaymentStatus;
import com.group3.bikehub.entity.Enum.ReferenceType;
import com.group3.bikehub.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;

    @PostMapping("/order")
    public ApiResponse<PaymentCreationResponse> createOrderPayment(
            @RequestBody OrderCreationRequest orderCreationRequest) {
        return ApiResponse.<PaymentCreationResponse>builder()
                .result(paymentService.createOrderPayment(orderCreationRequest))
                .build();
    }

    @PostMapping("/subscription")
    ApiResponse<PaymentCreationResponse> createSubscriptionPayment(
            @RequestBody PaymentCreationRequest paymentCreationRequest){
        return ApiResponse.<PaymentCreationResponse>builder()
                .result(paymentService.createSubscriptionPayment(paymentCreationRequest))
                .build();
    }

    @GetMapping("/all")
    ApiResponse<List<PaymentResponse>> allPayments() {
        return ApiResponse.<List<PaymentResponse>>builder()
                .result(paymentService.getAll())
                .build();
    }

    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PageResponse<PaymentResponse>> pageablePayments(
            @ModelAttribute PaymentFilterRequest request
            ) {
        return ApiResponse.<PageResponse<PaymentResponse>>builder()
                .result(paymentService.getPagePayment(request))
                .build();
    }

    @GetMapping("/my-payment")
    ApiResponse<List<PaymentResponse>> myPayments() {
        return ApiResponse.<List<PaymentResponse>>builder()
                .result(paymentService.myPayment())
                .build();
    }






}
