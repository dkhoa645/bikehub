package com.group3.bikehub.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(unique = true, nullable = false)
    String username;
    String password;
    String name;
    @ManyToMany
    Set<Role> roles;
    String verificationToken;
    Date expiration;
    boolean verified;

    @OneToOne(cascade = CascadeType.ALL)
    Kyc kyc;
}
