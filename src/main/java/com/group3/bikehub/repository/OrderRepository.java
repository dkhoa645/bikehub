package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Order;
import org.springframework.data.repository.Repository;


public interface OrderRepository extends Repository<Order, String> {
    void save (Order order);
}
