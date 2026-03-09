package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.PlaceOrderRequest;
import com.group3.bikehub.dto.response.OrderResponse;
import com.group3.bikehub.service.OrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping("/order/create")
    public ApiResponse<OrderResponse> create(@RequestBody PlaceOrderRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.placeOrder(request))
                .build();
    }


}
