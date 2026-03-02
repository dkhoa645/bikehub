package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.SubscriptionCreationRequest;
import com.group3.bikehub.dto.response.SubscriptionResponse;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Enum.SubscriptionStatusEnum;
import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.Payment;
import com.group3.bikehub.entity.Plan;
import com.group3.bikehub.entity.Subscription;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.SubscriptionMapper;
import com.group3.bikehub.repository.ListingRepository;
import com.group3.bikehub.repository.PlanRepository;
import com.group3.bikehub.repository.SubscriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionService {
    SubscriptionRepository subscriptionRepository;
    SubscriptionMapper subscriptionMapper;
    ListingRepository listingRepository;
    PlanRepository planRepository;


    public SubscriptionResponse createSubscription(SubscriptionCreationRequest subscriptionCreationRequest) {
        Listing listing = listingRepository.findById(subscriptionCreationRequest.getListingId())
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_FOUND));
        Plan plan = planRepository.findById(subscriptionCreationRequest.getPlanId())
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));

        SubscriptionResponse subscriptionResponse = subscriptionMapper.toSubscriptionResponse(
                subscriptionRepository.save(
                        Subscription.builder()
                                .plan(plan)
                                .listing(listing)
                                .status(SubscriptionStatusEnum.PENDING)
                                .build()
                ));
        subscriptionResponse.setListingId(listing.getId());

        return subscriptionResponse;
    }

    public void handleSubscriptionPayment(Payment payment) {
        Subscription subscription = subscriptionRepository.findById(UUID.fromString(payment.getReferenceId()))
                .orElseThrow(()-> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        int days = subscription.getPlan().getDurationDays();
        int priority = subscription.getPlan().getPriority();

        Date listingDate = subscription.getListing().getExpiryAt();
        Date createDate = new Date();
        Date startDate = new Date();
        Date expiryDate = new Date();
        if(listingDate.before(new Date())) {
            expiryDate = Date.from(Instant.now().plus(days, ChronoUnit.DAYS));
        }else{
            startDate = Date.from(listingDate.toInstant().plus(1, ChronoUnit.DAYS));
            expiryDate = Date.from(startDate.toInstant().plus(days, ChronoUnit.DAYS));
        }

        subscription.setCreatedDate(createDate);
        subscription.setStartDate(startDate);
        subscription.setExpiredDate(expiryDate);
        subscription.setStatus(SubscriptionStatusEnum.ACTIVE);
        subscription.getListing().setExpiryAt(expiryDate);
        subscription.getListing().setStatus(ListingStatus.LIVE);
        subscription.getListing().setPriority(priority);
        subscriptionRepository.save(subscription);
    }

    public List<SubscriptionResponse> getSubscriptionByListing(UUID listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(()-> new AppException(ErrorCode.LISTING_NOT_FOUND));
        List<Subscription> subscriptions = listing.getSubscriptions();

        List<SubscriptionResponse> subscriptionResponseList = subscriptions.stream()
                .map(subscriptionMapper::toSubscriptionResponse)
                .toList();
        subscriptionResponseList.forEach(sub -> sub.setListingId(listing.getId()));

        return subscriptionResponseList;

    }
}
