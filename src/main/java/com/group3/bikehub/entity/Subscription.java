package com.group3.bikehub.entity;


import com.group3.bikehub.entity.Enum.SubscriptionStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    Listing listing;
    @ManyToOne(fetch = FetchType.LAZY)
    Plan plan;
    @Enumerated(EnumType.STRING)
    SubscriptionStatusEnum status;
    Date createdDate;
    Date startDate;
    Date expiredDate;
}
