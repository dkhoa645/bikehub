package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Inspection;
import com.group3.bikehub.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, UUID> {
    Inspection findByListing(Listing listing);
}
