package com.group3.bikehub.repository;

import com.group3.bikehub.entity.InspectionScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionScoreRepository extends JpaRepository<InspectionScore,Long> {
}
