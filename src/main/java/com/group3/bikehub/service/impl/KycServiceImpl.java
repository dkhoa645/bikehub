package com.group3.bikehub.service.impl;

import com.group3.bikehub.dto.request.KycUploadRequest;
import com.group3.bikehub.dto.response.KycDraftResponse;
import com.group3.bikehub.dto.response.KycResponse;
import com.group3.bikehub.entity.Enum.KycStatus;
import com.group3.bikehub.entity.Kyc;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.KycMapper;
import com.group3.bikehub.repository.KycRepository;
import com.group3.bikehub.repository.UserRepository;
import com.group3.bikehub.service.GoogleVisionService;
import com.group3.bikehub.service.KycService;
import com.group3.bikehub.service.ParseTextService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@NoArgsConstructor
public class KycServiceImpl implements KycService {
    @Autowired
    KycRepository kycRepository;
    @Autowired
    KycMapper kycMapper;
    @Autowired
    KycDraftStoreService kycDraftStoreService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GoogleVisionService googleVisionService;
    @Autowired
    ParseTextService parseTextService;


    @Override
    public KycDraftResponse upload(KycUploadRequest request) {
        String ocrTextFront = parseTextService.extractText(googleVisionService.ocr(request.getFront()));
        String ocrTextBack = parseTextService.extractText(googleVisionService.ocr(request.getBack()));
        KycResponse kycResponse = parseTextService.parseCccd(ocrTextFront);
        String draftId = kycDraftStoreService.save(kycResponse);
        KycDraftResponse response = new KycDraftResponse(
                draftId,
                kycResponse
        );
        return response;

    }

    public void save(String draftId) {
        KycResponse draft = kycDraftStoreService.get(draftId);

        if (draft == null) {
            throw new AppException(ErrorCode.DRAFT_NOT_FOUND);
        }

        Kyc kyc = kycMapper.toKyc(draft);
        kycRepository.save(kyc);

        kycDraftStoreService.remove(draftId);

    }
    public void deleteKycById(String id) {
        Kyc kyc = kycRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.KYC_NOT_FOUND));

        kyc.setUser(null);

        kycRepository.delete(kyc);
    }

    @Transactional
    public void confirmKyc(String username, String draftId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        KycResponse draft = kycDraftStoreService.get(draftId);
        if (draft == null) {
            throw new AppException(ErrorCode.DRAFT_NOT_FOUND);
        }

        if (kycRepository.existsByUserAndStatus(user, KycStatus.PENDING)) {
            throw new AppException(ErrorCode.KYC_ALREADY_EXISTS);
        }

        if (kycRepository.existsByUserAndStatus(user, KycStatus.VERIFIED)) {
            throw new AppException(ErrorCode.KYC_ALREADY_EXISTS);
        }

        Optional<Kyc> rejectedKyc =
                kycRepository.findByUserAndStatus(user, KycStatus.REJECTED);

        if (rejectedKyc.isPresent()) {
            Kyc existing = rejectedKyc.get();

            kycMapper.updateKycFromDraft(draft, existing);
            existing.setStatus(KycStatus.PENDING);
            existing.setSubmittedAt(LocalDateTime.now());

            kycRepository.save(existing);
            kycDraftStoreService.remove(draftId);
            return;
        }

        Kyc newKyc = kycMapper.toKyc(draft);
        newKyc.setUser(user);
        newKyc.setStatus(KycStatus.PENDING);
        newKyc.setSubmittedAt(LocalDateTime.now());

        kycRepository.save(newKyc);

        kycDraftStoreService.remove(draftId);
    }

    public List<Kyc> getAllKyc(){
        return kycRepository.findAll();
    }


    public void verifyKyc(String id, boolean approved) {
        Kyc kyc = kycRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.KYC_NOT_FOUND));
        if (approved) {
            kyc.setStatus(KycStatus.VERIFIED);

        } else {
            kyc.setStatus(KycStatus.REJECTED);
        }
        kyc.setVerifiedAt(LocalDateTime.now());
        kycRepository.save(kyc);
    }

}