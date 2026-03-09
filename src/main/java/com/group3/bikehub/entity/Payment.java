package com.group3.bikehub.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group3.bikehub.entity.Enum.PaymentStatus;
import com.group3.bikehub.entity.Enum.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    Long paymentId;


    @Enumerated(EnumType.STRING)
    PaymentType type;
    String referenceId;
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    @Column(name = "transaction_ref")
    String transactionRef;
    @Column(unique = true)
    Long payosOrderCode;
    @Column(name = "paid_at")
    LocalDateTime paidAt;
    @Column(name = "create_at")
    LocalDateTime createAt;
    @PrePersist
    protected void onCreate() {
        this.transactionRef = UUID.randomUUID().toString();
    }
    @ManyToOne
    User user;

}
