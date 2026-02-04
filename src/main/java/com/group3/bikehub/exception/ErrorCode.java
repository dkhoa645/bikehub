package com.group3.bikehub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1002, "You do not have permission", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_EXISTED(1004,"User cannot be found", HttpStatus.NOT_FOUND),
    INVALID_TOKEN(1005,"Invalid token", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_EXISTED(1006,"Role cannot be found", HttpStatus.NOT_FOUND),
    VERIFICATION_TOKEN_EXPIRED(1007,"Verification token expired", HttpStatus.UNAUTHORIZED),
    SEND_EMAIL_FAILED(1008,"Send email failed", HttpStatus.BAD_REQUEST),
    OCR_IMAGE_FAILED(1009,"OCR image failed", HttpStatus.BAD_REQUEST),
    DRAFT_NOT_FOUND(1010,"Draft data expired", HttpStatus.BAD_REQUEST),
    KYC_NOT_FOUND(1011,"Kyc data expired", HttpStatus.BAD_REQUEST),
    KYC_ALREADY_EXISTS(1012,"Kyc already exists", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_FOUND(1013,"Image not found", HttpStatus.NOT_FOUND),
    LISTING_NOT_FOUND(1014,"Listing not found", HttpStatus.NOT_FOUND),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
