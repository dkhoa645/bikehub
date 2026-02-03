package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.ListingImageType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "listing_images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingImage {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String secureUrl;
    @ManyToOne()
    Listing listing;
    @Column(name = "image_order")
    Integer imageOrder;
}
