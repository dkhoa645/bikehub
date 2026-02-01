package com.group3.bikehub.service;



import com.group3.bikehub.dto.request.KycUploadRequest;
import com.group3.bikehub.dto.response.KycDraftResponse;
import com.group3.bikehub.entity.Kyc;

import java.util.List;

public interface KycService {
      KycDraftResponse upload(KycUploadRequest request);
      void confirmKyc(String username, String draftId);
      void verifyKyc(String idNumber, boolean approved);
      List<Kyc> getAllKyc();
      void deleteKycById(String id);
}
