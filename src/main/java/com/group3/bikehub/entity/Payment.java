package com.group3.bikehub.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group3.bikehub.entity.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Payment {

    public Payment(Order order, BigDecimal amount, PaymentStatus status, String transactionRef, Long payosOrderCode, LocalDateTime paidAt) {
        this.order = order;
        this.amount = amount;
        this.status = status;
        this.transactionRef = transactionRef;
        this.payosOrderCode = payosOrderCode;
        this.paidAt = paidAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "transaction_ref")
    private String transactionRef;
    @Column(unique = true)
    private Long payosOrderCode;
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    @PrePersist
    protected void onCreate() {
        this.transactionRef = UUID.randomUUID().toString();
    }


}
