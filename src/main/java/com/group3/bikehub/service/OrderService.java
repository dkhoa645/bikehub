package com.group3.bikehub.service;


import com.group3.bikehub.dto.request.DeliveredConfirmRequest;
import com.group3.bikehub.dto.response.OrderResponse;
import com.group3.bikehub.dto.response.PageResponse;
import com.group3.bikehub.entity.*;
import com.group3.bikehub.entity.Enum.*;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.OrderMapper;
import com.group3.bikehub.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    
    OrderRepository orderRepository;
    CurrentUserService currentUserService;
    OrderMapper orderMapper;
    CloudinaryService cloudinaryService;




    public List<OrderResponse> getMyOrders() {
         User user = currentUserService.getCurrentUser();
         List<Order> orders = orderRepository.findBySeller(user);
         if(orders.isEmpty()){
             orders = orderRepository.findByBuyer(user);
         }
         return orders.stream()
                 .map(orderMapper::toResponse)
                 .toList();
    }

    public List<OrderResponse> getAllOrders() {
         return orderRepository.findAll()
                 .stream()
                 .map(orderMapper::toResponse)
                 .toList();
    }

    public OrderResponse getOrder(UUID id) {
         Order order = orderRepository.findOrderById(id)
                 .orElseThrow(()-> new AppException(ErrorCode.ORDER_NOT_FOUND));
         return orderMapper.toResponse(order);
    }

    public void confirmOrder(UUID id) {
         User user = currentUserService.getCurrentUser();
         Order order = orderRepository.findOrderById(id)
                 .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
         if(!order.getSeller().equals(user)){
             throw new AppException(ErrorCode.UNAUTHORIZED);
         }
        if(!order.getSellerStatus().equals(SellerStatus.PENDING)){
            throw new AppException(ErrorCode.ORDER_ALREADY_RESOLVED);
        }
         if(!order.getOrderStatus().equals(OrderStatus.PAID)){
             throw new AppException(ErrorCode.ORDER_UNPAID);
         }
         order.setSellerStatus(SellerStatus.ACCEPTED);
         order.setOrderStatus(OrderStatus.IN_TRANSIT);
         order.setExpiresAt(Date.from(Instant.now().plus(5, ChronoUnit.DAYS)));
         List<OrderLog>  orderLogs = new ArrayList<>();
         orderLogs.add(OrderLog.builder()
                         .createdAt(Date.from(Instant.now()))
                         .status(OrderStatus.IN_TRANSIT)
                         .order(order)
                 .build());
         order.setLogs(orderLogs);
         orderRepository.save(order);
    }

    @Transactional
    public void rejectOrder(UUID id) {
        User user = currentUserService.getCurrentUser();
        Order order = orderRepository.findOrderById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if(!order.getSeller().equals(user)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if(!order.getSellerStatus().equals(SellerStatus.PENDING)){
            throw new AppException(ErrorCode.ORDER_ALREADY_RESOLVED);
        }
        if(!order.getOrderStatus().equals(OrderStatus.PAID)){
            throw new AppException(ErrorCode.ORDER_UNPAID);
        }
        OrderLog orderLog = OrderLog.builder()
                .order(order)
                .createdAt(new Date())
                .status(OrderStatus.CONFIRMED)
                .build();
        order.getLogs().add(orderLog);
        order.setSellerStatus(SellerStatus.REJECTED);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setExpiresAt(null);

        orderRepository.save(order);
    }


    public OrderResponse deliveredOrder(UUID id, DeliveredConfirmRequest deliveredConfirmRequest) {
        User user = currentUserService.getCurrentUser();
        Order order = orderRepository.findOrderById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if(!order.getSeller().equals(user)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if(!order.getSellerStatus().equals(SellerStatus.ACCEPTED)){
            throw new AppException(ErrorCode.ORDER_ACCEPT);
        }
        if(!order.getOrderStatus().equals(OrderStatus.IN_TRANSIT)){
            throw new AppException(ErrorCode.ORDER_IN_TRANSIT);
        }

        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setExpiresAt(Date.from(Instant.now().plus(5, ChronoUnit.DAYS)));
        try {
            OrderLog orderLog = OrderLog.builder()
                    .order(order)
                    .createdAt(Date.from(Instant.now()))
                    .status(OrderStatus.DELIVERED)
                    .image((String)cloudinaryService
                            .uploadFile(deliveredConfirmRequest.getFile(), "delivery")
                            .get("secure_url"))
                    .build();
            order.getLogs().add(orderLog);
            order = orderRepository.save(order);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return  orderMapper.toResponse(order);
    }

    public OrderResponse claimOrder(UUID id) {
        Order order =  orderRepository.findOrderById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        User user = currentUserService.getCurrentUser();
        if(!order.getBuyer().equals(user)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if(!order.getOrderStatus().equals(OrderStatus.DELIVERED)){
            throw new AppException(ErrorCode.ORDER_DELIVERED);
        }

        order.setOrderStatus(OrderStatus.COMPLETE);
        order.getListing().setStatus(ListingStatus.SOLD);
        OrderLog orderLog = OrderLog.builder()
                .order(order)
                .status(OrderStatus.COMPLETE)
                .createdAt(Date.from(Instant.now()))
                .build();
        order.getLogs().add(orderLog);
        order = orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    public PageResponse<OrderResponse> getPageOrder(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        var pageDate = orderRepository.findAll(pageable);

        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .totalElements(pageDate.getTotalElements())
                .pageSize(pageDate.getSize())
                .totalPage(pageDate.getTotalPages())
                .data(pageDate.getContent().stream()
                        .map(orderMapper::toResponse)
                        .toList())
                .build();
    }
}






