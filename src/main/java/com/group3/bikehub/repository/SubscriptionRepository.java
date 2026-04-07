package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Enum.SubscriptionStatusEnum;
import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findByListing(Listing listing);

    List<Subscription> findByStatus(SubscriptionStatusEnum status);

    List<Subscription> findByStatusAndExpiredDate(SubscriptionStatusEnum status, Date expiredDate);

    List<Subscription> findByStatusAndExpiredDateBefore(SubscriptionStatusEnum status, Date expiredDateBefore);
}
