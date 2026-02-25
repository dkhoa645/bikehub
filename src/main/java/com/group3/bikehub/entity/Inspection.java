package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.InspectionResult;
import com.group3.bikehub.entity.Enum.InspectionStatus;
import com.group3.bikehub.entity.Enum.InspectionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "inspections")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID inspectionId;

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    Listing listing;

    @ManyToOne
    @JoinColumn(name = "inspector_id")
    User inspector;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    InspectionType inspectionType;

    @ManyToOne
    @JoinColumn(name = "inspection_location_id", nullable = false)
    InspectionLocation location;

    Date scheduledAt;

    @Enumerated(EnumType.STRING)
    InspectionStatus status;

    @Enumerated(EnumType.STRING)
    InspectionResult inspectionResult;

    Integer overallScore;

    @Column(columnDefinition = "text")
    String rejectReason;

    Date createdAt;

    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL)
    List<InspectionScore> scores;
}
