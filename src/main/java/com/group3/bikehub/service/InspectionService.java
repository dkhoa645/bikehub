package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.InspectionCreationRequest;
import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.entity.*;
import com.group3.bikehub.entity.Enum.InspectionLocationType;
import com.group3.bikehub.entity.Enum.InspectionStatus;
import com.group3.bikehub.entity.Enum.InspectionType;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.InspectionLocationMapper;
import com.group3.bikehub.mapper.InspectionMapper;
import com.group3.bikehub.repository.AddressRepository;
import com.group3.bikehub.repository.InspectionLocationRepository;
import com.group3.bikehub.repository.InspectionRepository;
import com.group3.bikehub.repository.ListingRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionService {
    InspectionRepository inspectionRepository;
    InspectionMapper inspectionMapper;
    ListingRepository listingRepository;
    InspectionLocationRepository inspectionLocationRepository;
    InspectionLocationMapper inspectionLocationMapper;
    CurrentUserService currentUserService;
    AddressRepository addressRepository;

    @Transactional
    public InspectionResponse createInspection(InspectionCreationRequest request) {
        Inspection inspection = inspectionMapper.toInspection(request);
        inspection.setStatus(InspectionStatus.PENDING_ASSIGNED);
        Listing listing = listingRepository.findById(request.getListingId())
                .orElseThrow(() -> new AppException(ErrorCode.DRAFT_NOT_FOUND));
        listing.setStatus(ListingStatus.PENDING);
        inspection.setListing(listing);
        inspection.setCreatedAt(new Date());
        InspectionLocation location = new InspectionLocation() ;
        if (request.getInspectionType().equals(InspectionType.ONSITE)) {
            User user = currentUserService.getCurrentUser();
            Address address = addressRepository.findByUser(user);
            location.setType(InspectionLocationType.SELLER);
            location.setContactName(address.getNameContact());
            location.setContactPhone(address.getPhoneContact());
            location.setAddressLine(address.getAddressLine());
            location = inspectionLocationRepository.save(location);
        } else{
            location = inspectionLocationRepository.findById(request.getInspectionLocationId())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));
        }
        inspection.setLocation(location);

        return inspectionMapper.toInspectionResponse(inspectionRepository.save(inspection));
    }

    public InspectionResponse getInspection(UUID listingId) {
        Listing listing = listingRepository.findById(listingId).orElse(null);
        Inspection inspection = new Inspection();
        if (listing != null) {
            inspection = inspectionRepository.findByListing(listing);
        }
        return inspectionMapper.toInspectionResponse(inspection);
    }


    public List<InspectionResponse> getAll() {
        return inspectionRepository.findAll().stream()
                .map(inspectionMapper::toInspectionResponse)
                .toList();
    }
}
