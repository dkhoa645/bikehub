package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing,UUID> {
    List<Listing> findBySeller(User seller);

    List<Listing> findByFrameNumber(String frameNumber);
}
