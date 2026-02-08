package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Enum.InspectionLocationType;
import com.group3.bikehub.entity.InspectionLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface InspectionLocationRepository extends JpaRepository<InspectionLocation, UUID> {
    List<InspectionLocation> findByType(InspectionLocationType inspectionLocationType);
}
