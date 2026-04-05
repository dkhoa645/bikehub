package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.ScoreCreationRequest;
import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.entity.*;
import com.group3.bikehub.entity.Enum.*;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.InspectionMapper;
import com.group3.bikehub.repository.InspectionRepository;
import com.group3.bikehub.repository.ListingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionScoreService {

    InspectionRepository inspectionRepository;
    CloudinaryService cloudinaryService;
    InspectionMapper inspectionMapper;
    CurrentUserService currentUserService;
    ListingRepository listingRepository;

    public InspectionResponse createScore(UUID inspectionId, ScoreCreationRequest scoreCreationRequest) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new AppException(ErrorCode.INSPECTION_NOT_FOUND));

        User user = currentUserService.getCurrentUser();
        if (!inspection.getInspector().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (inspection.getScore() != null) {
            throw new AppException(ErrorCode.SCORE_ALREADY);
        }

        if (inspection.getScheduledAt().before(Date.from(
                Instant.now().plus(2, ChronoUnit.HOURS)))) {
            inspection.setStatus(InspectionStatus.EXPIRED);
            throw new AppException(ErrorCode.INSPECTION_EXPIRED);
        }


        Subscription subscription = inspection.getListing().getSubscriptions().stream()
                .filter(sub->{
                    return sub.getStatus().equals(SubscriptionStatusEnum.ACTIVE);
                })
                .toList()
                .getFirst();

        Plan plan = subscription.getPlan();
        Date expirationDate = Date.from(Instant.now().plus(plan.getDurationDays(), ChronoUnit.DAYS));

        inspection.setScore(scoreCreationRequest.getScore());
        if (inspection.getScore() < 5) {
            inspection.setInspectionResult(InspectionResult.FAILED);
        } else {
            inspection.setStatus(InspectionStatus.COMPLETED);
            inspection.setInspectionResult(InspectionResult.PASSED);
            Listing listing = inspection.getListing();
            listing.setStatus(ListingStatus.LIVE);
            listing.setExpiryAt(expirationDate);
            listing.setPriority(plan.getPriority());
            subscription.setStartDate(Date.from(Instant.now()));
            subscription.setExpiredDate(expirationDate);
            listingRepository.save(listing);
        }

        List<InspectionImage> list = new ArrayList<>();
        int i = 0;
        for (MultipartFile file : scoreCreationRequest.getFiles()) {
            Map res = null;
            try {
                //lưu lên cloud
                res = cloudinaryService.uploadFile(file, "inspection");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //thêm vào list
            list.add(
                    InspectionImage.builder()
                            .inspection(inspection)
                            .type(InspectionImageType.values()[i++])
                            .url((String) res.get("secure_url"))
                            .build()
            );
        }
        inspection.setComment(scoreCreationRequest.getComment());
        inspection.setImages(list);
        return inspectionMapper.toInspectionResponse(inspectionRepository.save(inspection));
    }

}
