package com.group3.bikehub.repository;

import com.group3.bikehub.entity.OrderLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLocationRepository extends JpaRepository<OrderLocation,Long> {
}
