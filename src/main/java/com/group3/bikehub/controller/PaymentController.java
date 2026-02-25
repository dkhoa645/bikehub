package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.CreatePaymentRequest;
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
    @PostMapping("/payment/create")
    public ApiResponse<Map<String, Object>> create(
            @RequestBody CreatePaymentRequest request) {
        return ApiResponse.<Map<String, Object>>builder()
                .message("Payment created successfully")
                .result(paymentService.create(request))
                .build();
    }

}
