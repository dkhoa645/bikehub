package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.ScoreCreationRequest;
import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.entity.Enum.InspectionResult;
import com.group3.bikehub.entity.Enum.InspectionStatus;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Inspection;
import com.group3.bikehub.entity.InspectionImage;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.InspectionMapper;
import com.group3.bikehub.repository.InspectionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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

    public InspectionResponse createScore(UUID inspectionId, ScoreCreationRequest scoreCreationRequest) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new AppException(ErrorCode.INSPECTION_NOT_FOUND));

        if(inspection.getScore() != null) {
            throw new AppException(ErrorCode.SCORE_ALREADY);
        }

        if(inspection.getScheduledAt().after(Date.from(
                Instant.now().plus(2,ChronoUnit.HOURS)))){
            inspection.setStatus(InspectionStatus.EXPIRED);
            throw new AppException(ErrorCode.INSPECTION_EXPIRED);
        }

        inspection.setStatus(InspectionStatus.COMPLETED);

        inspection.getListing().setStatus(ListingStatus.LIVE);

        inspection.setScore(scoreCreationRequest.getScore());
        if (inspection.getScore() < 5) {
            inspection.setInspectionResult(InspectionResult.FAILED);
        } else {
            inspection.setInspectionResult(InspectionResult.PASSED);
        }

        List<InspectionImage> list = new ArrayList<>();
        scoreCreationRequest.getInspectionImageCreations().stream().forEach(
                inspectionImage -> {
                    try {
                        //gửi lên cloudinary
                        Map res = cloudinaryService.uploadFile(inspectionImage.getFile(), "inspection");
                        //Lưu ảnh vào list
                        list.add(InspectionImage.builder()
                            .inspection(inspection)
                            .type(inspectionImage.getType())
                            .url((String) res.get("secure_url"))
                            .build());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        inspection.setComment(scoreCreationRequest.getComment());
        inspection.setImages(list);

        return inspectionMapper.toInspectionResponse(inspectionRepository.save(inspection));
    }
}
