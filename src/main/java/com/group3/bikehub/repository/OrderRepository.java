package com.group3.bikehub.repository;

import com.group3.bikehub.dto.response.OrderResponse;
import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Order;
import com.group3.bikehub.entity.User;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface OrderRepository extends Repository<Order, UUID> {
        Order save (Order order);

        Optional<Order> findOrderById(UUID id);
        List<Order> findByOrderStatusAndExpiresAtBefore(OrderStatus orderStatus, LocalDateTime created_at);

        List<Order> findBySeller(User seller);

        List<Order> findByBuyer(User buyer);

        List<Order> findAll();
}
