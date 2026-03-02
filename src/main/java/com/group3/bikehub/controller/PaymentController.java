package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.CreateListingPaymentRequest;
import com.group3.bikehub.dto.request.CreateOrderPaymentRequest;
import com.group3.bikehub.dto.request.PaymentCreationRequest;
import com.group3.bikehub.dto.response.PaymentCreationResponse;
import com.group3.bikehub.dto.response.PaymentResponse;
import com.group3.bikehub.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PaymentController {
    @Autowired
    private PaymentService paymentService;


    @PostMapping("/payment/create/order")
    public ApiResponse<Map<String, Object>> createOrderPayment(
            @RequestBody CreateOrderPaymentRequest request) {
        return ApiResponse.<Map<String, Object>>builder()
                .message("Payment created successfully")
                .result(paymentService.createOrderPayment(request))
                .build();
    }

    @PostMapping("/payment/create/subscription")
    ApiResponse<PaymentCreationResponse> createSubscriptionPayment(
            @RequestBody PaymentCreationRequest paymentCreationRequest
    ){
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




}
