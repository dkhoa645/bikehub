package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.OrderItem;
import org.springframework.data.repository.Repository;

import java.util.UUID;


public interface OrderItemRepository extends Repository<OrderItem, String> {
    void save (OrderItem orderItem);

    OrderItem findOrderItemByListing(Listing listing);
    void deleteById(UUID id );
}
