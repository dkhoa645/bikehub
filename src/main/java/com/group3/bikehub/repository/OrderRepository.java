package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Order;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface OrderRepository extends Repository<Order, String> {
    void save (Order order);

   Order findOrderById(Long id);
   List<Order> findByOrderStatusAndExpiresAtBefore(OrderStatus orderStatus, LocalDateTime created_at);
}
