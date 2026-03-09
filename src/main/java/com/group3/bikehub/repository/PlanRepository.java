package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {
    boolean existsByNameAndDurationDays(String name, Integer durationDays);
}
