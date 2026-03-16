package com.group3.bikehub.controller;

import com.cloudinary.Api;
import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.response.OrderLogResponse;
import com.group3.bikehub.entity.OrderLog;
import com.group3.bikehub.repository.OrderLogRepository;
import com.group3.bikehub.service.OrderLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/order-log")
public class OrderLogController {
    OrderLogService orderLogService;

    @GetMapping("/order/{orderId}")
    ApiResponse<List<OrderLogResponse>> getOrderLog(@PathVariable UUID orderId) {
        return ApiResponse.<List<OrderLogResponse>>builder()
                .result(orderLogService.getByListing(orderId))
                .build();
    }

    @GetMapping("/{orderLogId}")
    ApiResponse<OrderLogResponse> getOrderLogById(@PathVariable Long orderLogId) {
        return ApiResponse.<OrderLogResponse>builder()
                .result(orderLogService.getById(orderLogId))
                .build();
    }
}
