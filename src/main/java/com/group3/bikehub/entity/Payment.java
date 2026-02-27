package com.group3.bikehub.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group3.bikehub.entity.Enum.PaymentStatus;
import com.group3.bikehub.entity.Enum.PaymentType;
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

    public Payment( BigDecimal amount, PaymentStatus status, String transactionRef, Long payosOrderCode, LocalDateTime paidAt) {
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


    @Enumerated(EnumType.STRING)
    private PaymentType type;
    private String referenceId;
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
