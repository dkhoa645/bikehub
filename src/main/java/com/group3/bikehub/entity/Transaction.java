package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.ReferenceType;
import com.group3.bikehub.entity.Enum.TransactionStatus;
import com.group3.bikehub.entity.Enum.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    User fromUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    User toUser;
    BigDecimal amount;
    @Enumerated(EnumType.STRING)
    TransactionStatus status;
    @Enumerated(EnumType.STRING)
    TransactionType type;
    @Enumerated(EnumType.STRING)
    ReferenceType referenceType;
    UUID referenceId;
    Date createdAt;
}
