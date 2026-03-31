package com.group3.bikehub.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.group3.bikehub.dto.response.OrderLocationResponse;
import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Enum.SellerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    User buyer;
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    User seller;
    @Enumerated(EnumType.STRING)
    SellerStatus sellerStatus;
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;
    @JsonManagedReference
    @ManyToOne
    Listing listing;
    BigDecimal depositAmount;
    BigDecimal totalAmount;
    Date createdAt;
    Date expiresAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderLog> logs;
    @OneToOne(cascade =  CascadeType.ALL)
    OrderLocation orderLocation;
}
