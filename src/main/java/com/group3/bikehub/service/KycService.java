package com.group3.bikehub.service;



import com.group3.bikehub.dto.request.KycUploadRequest;
import com.group3.bikehub.dto.response.KycDraftResponse;
import com.group3.bikehub.dto.response.KycResponse;
import com.group3.bikehub.entity.Kyc;
import com.group3.bikehub.entity.User;

import java.util.List;

public interface KycService {
      KycDraftResponse upload(KycUploadRequest request);
      void confirmKyc(String username, String draftId);
      void verifyKyc(String idNumber, boolean approved);
      List<KycResponse> getAllKyc();

      boolean isKyc(User user);

      void deleteKycById(String id);
}
