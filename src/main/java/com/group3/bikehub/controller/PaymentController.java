package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.CreateListingPaymentRequest;
import com.group3.bikehub.dto.request.CreateOrderPaymentRequest;
import com.group3.bikehub.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/payment/create/listing")
    public ApiResponse<Map<String, Object>> createListingPayment(
            @RequestBody CreateListingPaymentRequest request) {
        return ApiResponse.<Map<String, Object>>builder()
                .message("Payment created successfully")
                .result(paymentService.createListingPayment(request))
                .build();
    }





}
