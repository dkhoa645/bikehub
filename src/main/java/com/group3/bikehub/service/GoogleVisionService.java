package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.KycRequest;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.List;

@Service
public class GoogleVisionService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GOOGLE_VISION_URL =
            "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyDT0HbWymUMTsuBz8TWhG4-o11laOueJmw";

    public String ocr(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image is empty");
        }

        try {

            String base64Image = Base64.getEncoder()
                    .encodeToString(image.getBytes());

            KycRequest request = new KycRequest(
                    List.of(
                            new KycRequest.Request(
                                    new KycRequest.Image(base64Image),
                                    List.of(new KycRequest.Feature("TEXT_DETECTION"))
                            )
                    )
            );

            String jsonBody = objectMapper.writeValueAsString(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            String googleResponse = restTemplate
                    .postForEntity(GOOGLE_VISION_URL, entity, String.class)
                    .getBody();
            return googleResponse;


        } catch (Exception e) {
            throw new AppException(ErrorCode.OCR_IMAGE_FAILED);
        }

    }

}