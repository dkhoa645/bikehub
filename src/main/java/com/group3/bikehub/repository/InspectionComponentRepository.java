package com.group3.bikehub.repository;

import com.group3.bikehub.entity.InspectionComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionComponentRepository extends JpaRepository<InspectionComponent, Long> {
    boolean existsByName(String name);
}
