package com.group3.bikehub.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "inspection_scores")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "inspection_id", nullable = false)
    Inspection inspection;

    @ManyToOne
    @JoinColumn(name = "component_id", nullable = false)
    InspectionComponent component;

    Integer score;
    String note;
}
