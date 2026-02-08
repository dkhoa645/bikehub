package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.InspectionLocationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "inspection_locations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "inspection_location_id")
    UUID id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    InspectionLocationType type;
    @Column(name = "contact_name")
    String contactName;
    @Column(name = "contact_phone", nullable = false)
    String contactPhone;
    @Column(name = "address_line", nullable = false)
    String addressLine;
    @Column(columnDefinition = "text")
    String note;
}
