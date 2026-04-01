package com.group3.bikehub.repository;

import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.entity.Enum.InspectionStatus;
import com.group3.bikehub.entity.Inspection;
import com.group3.bikehub.entity.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, UUID> {
    Inspection findByListing(Listing listing);

    List<Inspection> findByInspectorId(UUID inspectorId);

    List<Inspection> findByInspectorIdOrderByCreatedAt(UUID inspectorId);

    List<Inspection> findByStatusOrderByCreatedAt(InspectionStatus status);

    Page<Inspection> findAll(Pageable pageable);

    List<Inspection> findByInspectorIdOrderByCreatedAtDesc(UUID inspectorId);

    @Query("""
    SELECT i FROM Inspection i
    ORDER BY 
        CASE 
            WHEN i.status = 'PENDING_ASSIGNED' THEN 1
            ELSE 2
        END,
        i.scheduledAt ASC
""")
    List<Inspection> findPriorityInspections();
}
