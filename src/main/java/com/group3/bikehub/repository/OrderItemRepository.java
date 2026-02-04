package com.group3.bikehub.repository;

import com.group3.bikehub.entity.OrderItem;
import org.springframework.data.repository.Repository;


public interface OrderItemRepository extends Repository<OrderItem, String> {
    void save (OrderItem orderItem);
}
