package com.group3.bikehub.service;

import com.group3.bikehub.dto.response.KycResponse;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParseTextService {
    private final ObjectMapper objectMapper = new ObjectMapper();
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

    public KycResponse parseCccd(String text) {
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
    public String extractOptional(String text, String regex) {
        Matcher matcher = Pattern
                .compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
                .matcher(text);

        if (matcher.find()) {
            return matcher.group(matcher.groupCount()).trim();
        }
        return null;
    }

    public String extract(String text, String regex) {
        Matcher matcher = Pattern
                .compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
                .matcher(text);

        if (matcher.find()) {
            return matcher.group(matcher.groupCount()).trim();
        }
        throw new AppException(ErrorCode.OCR_IMAGE_FAILED);
    }


    public String extractNextLine(String text, String key) {
        String[] lines = text.split("\\n");

        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].contains(key)) {
                return lines[i + 1].trim();
            }
        }

        throw new AppException(ErrorCode.OCR_IMAGE_FAILED);
    }



}
