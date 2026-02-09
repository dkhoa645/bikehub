package com.group3.bikehub.service;


import com.group3.bikehub.dto.request.AcceptOrderRequest;
import com.group3.bikehub.dto.request.PlaceOrderRequest;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Enum.SellerStatus;
import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.Order;
import com.group3.bikehub.entity.OrderItem;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.repository.ListingRepository;
import com.group3.bikehub.repository.OrderItemRepository;
import com.group3.bikehub.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        order.setUser(user);
        order.setTotal_ammount(listing.orElseThrow().getPrice());;
        order.setCreated_at(LocalDateTime.now());

        OrderItem orderItem = new OrderItem();
        orderItem.setListing(listing.get());
        orderItem.setDescription(request.getDescription());
        orderItem.setOrder(order);
        orderItem.setStatus(SellerStatus.PENDING);
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.save(order);
        orderItemRepository.save(orderItem);
    }
    public void acceptOrder(AcceptOrderRequest request) {
      Optional<Listing> listing = listingRepository.findById(request.getListingId());
     OrderItem orderItem = orderItemRepository.findOrderItemByListing(listing.get());
            if (orderItem.getStatus().equals(OrderStatus.CANCELLED)) {
                throw new AppException(ErrorCode.ORDER_CANCELED);
        }


     if (request.isAccepted()){
         orderItem.setStatus(SellerStatus.ACCEPTED);
     } else{
         orderItem.setStatus(SellerStatus.CANCELLED);

     }

    }


}
