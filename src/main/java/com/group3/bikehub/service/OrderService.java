package com.group3.bikehub.service;


import com.group3.bikehub.dto.request.AcceptOrderRequest;
import com.group3.bikehub.dto.request.PlaceOrderRequest;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CurentUserService curentUserService;
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public void placeOrder(PlaceOrderRequest request) {
        User user = curentUserService.getCurrentUser();
        Optional<Listing> listing = listingRepository.findById(request.getListingId());
//        if (listing.get().getStatus().equals(ListingStatus.PENDING) || listing.get().getStatus().equals(ListingStatus.APPROVED) || listing.get().getStatus().equals(ListingStatus.REJECTED)) {
//
//         throw new AppException(ErrorCode.LISTING_NOT_FOUND);
//        }

        Order order = new Order();
        order.setBuyer(user);
        order.setSeller(listing.get().getSeller());
        order.setTotal_ammount(listing.orElseThrow().getPrice());;
        order.setCreated_at(LocalDateTime.now());

        OrderItem orderItem = new OrderItem();
        orderItem.setListing(listing.get());
        orderItem.setDescription(request.getDescription());
        orderItem.setOrder(order);
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.save(order);
        orderItemRepository.save(orderItem);
    }
    public void acceptOrder(AcceptOrderRequest request) {
     Order order = orderRepository.findOrderById(request.getOrderId());
            if (order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
                throw new AppException(ErrorCode.ORDER_CANCELED);
        }
         List<Payment> payment = order.getPayments();

                for (Payment pay : payment) {
                    if (pay.getStatus() == PaymentStatus.PAID) {
                        if (request.isAccepted()) {
                            order.setSellerStatus(SellerStatus.ACCEPTED);
                            return;
                        } else {

                            order.setSellerStatus(SellerStatus.CANCELLED);
                            return;
                        }

                    }
                }
                throw new AppException(ErrorCode.ORDER_UNPAID);





     }


    }




