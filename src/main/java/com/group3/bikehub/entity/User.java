package com.group3.bikehub.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(unique = true, nullable = false)
    String username;
    String password;
    String name;
    @ManyToMany
    Set<Role> roles;
    String verificationToken;
    Date expiration;
    boolean verified;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Address address;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Kyc kycProfile;

}
