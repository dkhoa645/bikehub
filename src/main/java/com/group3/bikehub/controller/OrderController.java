package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.PlaceOrderRequest;
import com.group3.bikehub.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/order/create")
    public ApiResponse<Void> create(@RequestBody PlaceOrderRequest request) {
        orderService.placeOrder(request);
        return ApiResponse.<Void>builder().message("place order success").build();
    }


}
