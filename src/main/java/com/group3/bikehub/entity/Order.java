package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User user;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private BigDecimal total_ammount;
    private LocalDateTime created_at;
}
