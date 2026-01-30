package com.group3.bikehub.service.impl;


import com.group3.bikehub.dto.request.KycRequest;
import com.group3.bikehub.dto.response.KycResponse;
import com.group3.bikehub.entity.Kyc;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.KycMapper;
import com.group3.bikehub.repository.KycRepository;
import com.group3.bikehub.service.KycService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@NoArgsConstructor
public class KycServiceImpl implements KycService {
    @Autowired
    KycRepository kycRepository;
    @Autowired
    KycMapper kycMapper;
    @Autowired
    KycDraftStoreService kycDraftStoreService;

    private static final String GOOGLE_VISION_URL =
            "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyDT0HbWymUMTsuBz8TWhG4-o11laOueJmw";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public KycResponse ocr(MultipartFile image) {

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

            String ocrText = extractText(googleResponse);

            return parseCccd(ocrText);

        } catch (Exception e) {
            throw new AppException(ErrorCode.OCR_IMAGE_FAILED);
        }
    }



    public String extractText(String responseJson) {

        JsonNode root = objectMapper.readTree(responseJson);

        JsonNode textNode = root
                .path("responses")
                .get(0)
                .path("textAnnotations")
                .get(0)
                .path("description");

        return textNode.isMissingNode() ? "" : textNode.asText();
    }



    private KycResponse parseCccd(String text) {
        String expiryDate = extractOptional(text, "(?:Có giá trị đến|Date of expiry).*?(\\d{2}/\\d{2}/\\d{4})"
        );

        if (expiryDate == null) {
            expiryDate = extract(text, "(?:Có giá trị đến|Date of expiry)[\\s\\S]*?(\\d{2}/\\d{2}/\\d{4})");
        }
        KycResponse response = new  KycResponse(
                extract(
                        text,
                        "(?:Số|So|Só)[/\\s]*No\\.?:\\s*(\\d{12})"),
                extractNextLine(text, "Họ và tên"),
                extract(text, "(Ngày sinh|Date of birth).*?:\\s*(\\d{2}/\\d{2}/\\d{4})"),
                extract(text, "(Giới tính|Sex).*?(Nam|Nữ)"),
                extract(text, "Nationality:\\s*([A-Za-zÀ-ỹ ]+)"),
                extractNextLine(text, "Place of origin"),
                extract(text, "(?:Nơi thường trú|Place of residence).*?:\\s*([\\s\\S]+?)(?:\\n(?:Có giá trị|Date of expiry)|$)"),
                expiryDate);

        return response;
    }
    private String extractOptional(String text, String regex) {
        Matcher matcher = Pattern
                .compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
                .matcher(text);

        if (matcher.find()) {
            return matcher.group(matcher.groupCount()).trim();
        }
        return null;
    }

    private String extract(String text, String regex) {
        Matcher matcher = Pattern
                .compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
                .matcher(text);

        if (matcher.find()) {
            return matcher.group(matcher.groupCount()).trim();
        }
        throw new AppException(ErrorCode.OCR_IMAGE_FAILED);
    }


    private String extractNextLine(String text, String key) {
        String[] lines = text.split("\\n");

        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].contains(key)) {
                return lines[i + 1].trim();
            }
        }

        throw new AppException(ErrorCode.OCR_IMAGE_FAILED);
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

}




