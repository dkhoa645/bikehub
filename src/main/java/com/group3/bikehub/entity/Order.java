package com.group3.bikehub.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Enum.SellerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
     List<OrderItem> items;
     BigDecimal total_amount;
     LocalDateTime created_at;
     LocalDateTime expiresAt;
}
