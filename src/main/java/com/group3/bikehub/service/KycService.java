package com.group3.bikehub.service;



import com.group3.bikehub.dto.response.KycResponse;
import org.springframework.web.multipart.MultipartFile;

public interface KycService {
      KycResponse ocr(MultipartFile image);
      String extractText(String responseJson);
      void confirmKyc(String username, String draftId);

}
