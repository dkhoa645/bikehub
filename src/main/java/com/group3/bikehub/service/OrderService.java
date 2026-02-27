package com.group3.bikehub.service;


import com.group3.bikehub.dto.request.AcceptOrderRequest;
import com.group3.bikehub.dto.request.PlaceOrderRequest;
import com.group3.bikehub.dto.response.OrderResponse;
import com.group3.bikehub.entity.*;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Enum.PaymentStatus;
import com.group3.bikehub.entity.Enum.SellerStatus;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.repository.ListingRepository;
import com.group3.bikehub.repository.OrderItemRepository;
import com.group3.bikehub.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CurrentUserService curentUserService;
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderResponse placeOrder(PlaceOrderRequest request) {
        User user = curentUserService.getCurrentUser();
        Optional<Listing> listing = listingRepository.findById(request.getListingId());
        if (listing.isEmpty()) {
            throw new AppException(ErrorCode.LISTING_NOT_FOUND);
        }

        if (!listing.get().getStatus().equals(ListingStatus.LIVE)) {
            throw new AppException(ErrorCode.LISTING_NOT_FOUND);
        }


        Order order = new Order();
        order.setBuyer(user);
        order.setSeller(listing.get().getSeller());
        order.setTotal_ammount(listing.orElseThrow().getPrice());

        order.setCreated_at(LocalDateTime.now());
        order.setExpiresAt(LocalDateTime.now().plusMinutes(1));

        OrderItem orderItem = new OrderItem();
        orderItem.setListing(listing.get());
        orderItem.setDescription(request.getDescription());
        orderItem.setOrder(order);
        order.setOrderStatus(OrderStatus.PENDING);
        listing.get().setStatus(ListingStatus.RESERVED);
        orderRepository.save(order);
        orderItemRepository.save(orderItem);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        return orderResponse;
        
    }
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void autoExpireOrders() {

        List<Order> expiredOrders =
                orderRepository.findByOrderStatusAndExpiresAtBefore(
                        OrderStatus.PENDING,
                        LocalDateTime.now()
                );

        for (Order order : expiredOrders) {

            // đổi trạng thái order
            order.setOrderStatus(OrderStatus.CANCELLED);

            // trả listing về LIVE
            Listing listing = order.getItems()
                    .get(0)
                    .getListing();

            if (listing.getStatus() == ListingStatus.RESERVED) {
                listing.setStatus(ListingStatus.LIVE);
            }
        }
    }
     public void handleOrderPayment(Payment payment){
        Long orderId = Long.valueOf(payment.getReferenceId());
        Order order = orderRepository.findOrderById(orderId);
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);
     }

    public void acceptOrder(AcceptOrderRequest request) {
        Order order = orderRepository.findOrderById(request.getOrderId());
        if (!order.getOrderStatus().equals(OrderStatus.PAID)) {
            throw new AppException(ErrorCode.ORDER_UNPAID);

        }
        if (request.isAccepted()){
            order.setSellerStatus(SellerStatus.ACCEPTED);
            orderRepository.save(order);
        }  else{
            order.setSellerStatus(SellerStatus.CANCELLED);
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.getItems().get(0).getListing().setStatus(ListingStatus.LIVE);
            orderRepository.save(order);
        }


    }

}




