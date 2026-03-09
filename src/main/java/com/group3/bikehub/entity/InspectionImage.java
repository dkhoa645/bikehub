package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.InspectionImageType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "inspection_images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    Inspection inspection;
    String url;
    @Enumerated(EnumType.STRING)
    InspectionImageType type;
}
