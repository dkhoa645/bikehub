package com.group3.bikehub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_location")
public class OrderLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String addressLine;
    String phoneContact;
    String nameContact;

    @OneToOne
    Order order;
}
