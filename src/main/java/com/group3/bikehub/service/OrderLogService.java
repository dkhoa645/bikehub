package com.group3.bikehub.service;

import com.group3.bikehub.dto.response.OrderLogResponse;
import com.group3.bikehub.entity.Order;
import com.group3.bikehub.entity.OrderLog;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.OrderLogMapper;
import com.group3.bikehub.repository.OrderLogRepository;
import com.group3.bikehub.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderLogService {
    OrderLogRepository orderLogRepository;
    OrderRepository orderRepository;
    OrderLogMapper orderLogMapper;


    public List<OrderLogResponse> getByListing(UUID orderId) {
        Order orders = orderRepository.findOrderById(orderId)
                .orElseThrow(()-> new AppException(ErrorCode.ORDER_NOT_FOUND));
        List<OrderLog> orderLogs = orders.getLogs().stream()
                .sorted(Comparator.comparing(OrderLog::getCreatedAt))
                .toList();
        return orderLogs.stream()
                .map(orderLogMapper::toResponse)
                .toList();
    }

    public OrderLogResponse getById(Long orderLogId) {
        OrderLog orderLog = orderLogRepository.findById(orderLogId)
                .orElseThrow(()-> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderLogMapper.toResponse(orderLog);
    }
}
