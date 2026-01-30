package com.group3.bikehub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
// day la` request cho OCR cua google k phai cho frontend
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KycRequest {

    private List<Request> requests;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Image image;
        private List<Feature> features;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Feature {
        private String type;
    }
}