package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing,UUID> {
    List<Listing> findBySeller(User seller);

    List<Listing> findBySellerOrderByCreatedAtDesc(User seller);
    
    List<Listing> findByFrameNumber(String frameNumber);

    @Query("""
        SELECT l
        FROM Listing l
        WHERE l.status = :status
        AND l.expiryAt > :now
            """)
    Page<Listing> findActiveListings(
            Pageable pageable,
            @Param("status") ListingStatus status,
            @Param("now") Date now);

    @Modifying
    @Query("""
        UPDATE Listing l
        SET l.status = 'RESOLVED'
        WHERE l.id = :listingId
        AND l.status = :currentStatus
        """)
    int resolveListing(UUID listingId, ListingStatus currentStatus);

    Page<Listing> findAll(Pageable pageable);
}
