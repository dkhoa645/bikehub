package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.SellerStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    Listing listing;
    String description;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
}
