package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.OrderStatus;
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
    UUID id;
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    User user;
    OrderStatus status;
    BigDecimal total_ammount;
    LocalDateTime created_at;
}
