package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.CreateListingPaymentRequest;
import com.group3.bikehub.dto.request.CreateOrderPaymentRequest;
import com.group3.bikehub.dto.request.PaymentCreationRequest;
import com.group3.bikehub.dto.response.PaymentCreationResponse;
import com.group3.bikehub.dto.response.PaymentResponse;
import com.group3.bikehub.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;

    @PostMapping("/order")
    public ApiResponse<Map<String, Object>> createOrderPayment(
            @RequestBody CreateOrderPaymentRequest request) {
        return ApiResponse.<Map<String, Object>>builder()
                .message("Payment created successfully")
                .result(paymentService.createOrderPayment(request))
                .build();
    }

    @PostMapping("/subscription")
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

    @GetMapping("/my-payment")
    ApiResponse<List<PaymentResponse>> myPayments() {
        return ApiResponse.<List<PaymentResponse>>builder()
                .result(paymentService.myPayment())
                .build();
    }






}
