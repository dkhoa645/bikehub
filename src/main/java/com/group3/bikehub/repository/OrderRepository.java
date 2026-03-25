package com.group3.bikehub.repository;

import com.group3.bikehub.dto.response.OrderResponse;
import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Enum.SellerStatus;
import com.group3.bikehub.entity.Order;
import com.group3.bikehub.entity.OrderLog;
import com.group3.bikehub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;


import java.time.LocalDateTime;
import java.util.*;


public interface OrderRepository extends Repository<Order, UUID> {
        Order save (Order order);

        Optional<Order> findOrderById(UUID id);

        List<Order> findByOrderStatusInAndExpiresAtBefore(List<OrderStatus> statuses, Date expiresAtBefore);

    List<Order> findBySellerOrderByCreatedAtDesc(User seller);

        List<Order> findByBuyerOrderByCreatedAtDesc(User buyer);

        List<Order> findAll();

    @Query("""
    SELECT COUNT(o)
    FROM Order o
    WHERE o.buyer.id = :userId
    AND o.sellerStatus = :sellerStatus
    AND o.orderStatus = :status
    AND DATE(o.createdAt) = CURRENT_DATE
""")
    int countExpiredOrdersByUser(UUID userId, OrderStatus status, SellerStatus sellerStatus);

    List<Order> findByOrderStatusAndSellerStatusAndExpiresAtBefore(OrderStatus orderStatus, SellerStatus sellerStatus, Date expiresAtBefore);

    List<Order> findByOrderStatusAndSellerStatus(OrderStatus orderStatus, SellerStatus sellerStatus);


    Page<Order> findAll(Pageable pageable);
}
