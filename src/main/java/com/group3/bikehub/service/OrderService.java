package com.group3.bikehub.service;


import com.group3.bikehub.dto.response.OrderResponse;
import com.group3.bikehub.entity.*;
import com.group3.bikehub.entity.Enum.*;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.OrderMapper;
import com.group3.bikehub.repository.OrderRepository;
import com.group3.bikehub.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;

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
    PaymentService paymentService;
    PaymentRepository paymentRepository;


    //    public OrderResponse placeOrder(PlaceOrderRequest request) {
//        User user = curentUserService.getCurrentUser();
//        Optional<Listing> listing = listingRepository.findById(request.getListingId());
//
//        if (listing.isEmpty()) {
//            throw new AppException(ErrorCode.LISTING_NOT_FOUND);
//        }
//
//        if (!listing.get().getStatus().equals(ListingStatus.LIVE)) {
//            throw new AppException(ErrorCode.LISTING_NOT_FOUND);
//        }
//
//        Order order = new Order();
//        order.setBuyer(user);
//        order.setSeller(listing.get().getSeller());
//        order.setTotal_amount(listing.orElseThrow().getPrice());
//        order.setCreated_at(LocalDateTime.now());
//        order.setExpiresAt(LocalDateTime.now().plusMinutes(1));
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setListing(listing.get());
//        orderItem.setDescription(request.getDescription());
//        orderItem.setOrder(order);
//        order.setOrderStatus(OrderStatus.PENDING);
//        listing.get().setStatus(ListingStatus.RESERVED);
//
//        orderRepository.save(order);
//        orderItemRepository.save(orderItem);
//        OrderResponse orderResponse = new OrderResponse();
//        orderResponse.setId(order.getId());
//        return orderResponse;
//
//    }



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
        order.setSellerStatus(SellerStatus.REJECTED);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setExpiresAt(null);

        orderRepository.save(order);
    }




}






