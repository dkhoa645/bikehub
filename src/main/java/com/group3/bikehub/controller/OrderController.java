package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.PlaceOrderRequest;
import com.group3.bikehub.dto.response.OrderResponse;
import com.group3.bikehub.service.OrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/order")
public class OrderController {

    OrderService orderService;

    @PutMapping("/{id}/accept")
    ApiResponse<Void> confirmOrder(@PathVariable UUID id){
        orderService.confirmOrder(id);
        return ApiResponse.<Void>builder()
                .message("Confirm Order Successful!!")
                .build();
    }

    @PutMapping("/{id}/reject")
    ApiResponse<Void> rejectOrder(@PathVariable UUID id){
        orderService.rejectOrder(id);
        return ApiResponse.<Void>builder()
                .message("Reject Order Successful!!")
                .build();
    }

    @GetMapping("/my-order")
    ApiResponse<List<OrderResponse>> getMyOrder(){
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getMyOrders())
                .build();
    }



    @GetMapping()
    ApiResponse<List<OrderResponse>> getAllOrder(){
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getAllOrders())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<OrderResponse> getOrder(@PathVariable UUID id){
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrder(id))
                .build();
    }


}
