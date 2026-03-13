package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.BankCode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String addressLine;
    String phoneContact;
    String nameContact;
    String accountNumber;
    BankCode bankCode;


    @OneToOne()
    @JoinColumn(name = "user_id", unique = true)
    User user;

}
