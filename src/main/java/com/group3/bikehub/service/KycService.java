package com.group3.bikehub.service;



import com.group3.bikehub.dto.response.KycResponse;
import com.group3.bikehub.entity.Kyc;
import org.springframework.web.multipart.MultipartFile;

public interface KycService {
      public KycResponse ocr(MultipartFile image);
      public String extractText(String responseJson);
      public void save(String draftId);

}
