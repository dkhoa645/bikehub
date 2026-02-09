package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.SellerStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;
    SellerStatus status;
    private String description;
    @ManyToOne
    private Order order;
}
