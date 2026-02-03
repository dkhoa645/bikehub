package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.ListingStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "listings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "listing_id")
    UUID id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    User seller;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    Brand brand;

    @ManyToOne
    @JoinColumn(name = "bike_type_id")
    BikeType bikeType;

    String title;
    Integer usageDuration;
    String frameNumber;

    @Column(columnDefinition = "text")
    String description;

    BigDecimal price;

    @Enumerated(EnumType.STRING)
    ListingStatus status;

    Date createdAt;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL)
    List<ListingImage> images;
}
