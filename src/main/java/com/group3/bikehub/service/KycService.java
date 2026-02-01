package com.group3.bikehub.service;



import com.group3.bikehub.dto.response.KycDraftResponse;
import com.group3.bikehub.entity.Kyc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface KycService {
      KycDraftResponse upload(MultipartFile image);
      void confirmKyc(String username, String draftId);
      void verifyKyc(String idNumber, boolean approved);
      List<Kyc> getAllKyc();
      void deleteKycById(String id);
}
