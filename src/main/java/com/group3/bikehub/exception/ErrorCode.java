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
    ORDER_CANCELED(1015,"Order canceled", HttpStatus.BAD_REQUEST),
    ORDER_UNPAID(1016, "Order UNPAID", HttpStatus.BAD_REQUEST),
    LOCATION_NOT_FOUND(1017,"Location not found", HttpStatus.NOT_FOUND),
    ADDRESS_EXIST(1018,"Address already exists", HttpStatus.BAD_REQUEST),
    LISTING_SOLD(1019,"Listing with sold status not available", HttpStatus.BAD_REQUEST),
    LISTING_STATUS(1019,"Listing with this status not available", HttpStatus.BAD_REQUEST),
    LOCATION_EXISTS(1020,"Location already exists", HttpStatus.BAD_REQUEST),
    INSPECTION_NOT_FOUND(1021,"Inspection not found", HttpStatus.NOT_FOUND),
    BRAND_NOT_FOUND(1022,"Brand not found", HttpStatus.NOT_FOUND),
    COMPONENT_EXISTS(1023,"Component already exists", HttpStatus.BAD_REQUEST),
    COMPONENT_NOT_FOUND(1024, "Component not found", HttpStatus.NOT_FOUND),
    INVALID_SELLER_ID(1025,"Invalid seller", HttpStatus.BAD_REQUEST),
    PLAN_NOT_FOUND(1026,"Plan not found", HttpStatus.NOT_FOUND),
    PLAN_EXISTS(1027,"Plan already exists", HttpStatus.BAD_REQUEST),
    SUBSCRIPTION_NOT_FOUND(1028,"Subscription not found", HttpStatus.NOT_FOUND),
    IMAGE_DUPLICATE(1029, "Image exist", HttpStatus.NOT_FOUND),
    TIME_BEFORE(1030, "Time must be after now", HttpStatus.BAD_REQUEST),
    INSPECTION_EXPIRED(1031,"Inspection date expired", HttpStatus.BAD_REQUEST),
    PRICE_MIN(1032, "Price must be at least 100.000vnd", HttpStatus.BAD_REQUEST),
    PRICE_MAX(1032, "Price max is 100.000.000vnd", HttpStatus.BAD_REQUEST),
    SCORE_MIN(1033, "Score min is 1", HttpStatus.BAD_REQUEST),
    SCORE_MAX(1033, "Score max is 10", HttpStatus.BAD_REQUEST),
    TITLE_MAX(1033, "Title max is 50", HttpStatus.BAD_REQUEST),
    SCORE_ALREADY(1034, "Score already", HttpStatus.BAD_REQUEST),
    FILE_MIN(1035, "Max 4 images", HttpStatus.BAD_REQUEST),
    CURRENT_PASSWORD(1036, "Your current password is invalid ", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(1037,"Order not found", HttpStatus.NOT_FOUND),
    FAVORITE_DUPLICATE(1038, "Listing already in Favorite", HttpStatus.BAD_REQUEST),
    LISTING_RESERVE(1039, "Listing is reserved", HttpStatus.BAD_REQUEST),
    ORDER_SPAMMING(1040, "Buyer spam order payment 5 time. Try tomorrow ", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_RESOLVED(1041, "Your Order has already resolved", HttpStatus.BAD_REQUEST),
    PAYOUT(1042, "Payout failed", HttpStatus.BAD_REQUEST),
    ORDER_ACCEPT(1043,"Order have to be accepted", HttpStatus.BAD_REQUEST),
    ORDER_IN_TRANSIT(1044,"Order have to be in transit", HttpStatus.BAD_REQUEST),
    FILE_NOT_NULL(1045, "Image can not be null", HttpStatus.BAD_REQUEST),
    ORDER_DELIVERED(1046,"Order must be deliverd", HttpStatus.BAD_REQUEST),
    INVALID_FRAME_NUMBER(1047, "Frame number does not contain special characters", HttpStatus.BAD_REQUEST),
    LISTING_IMAGE_NOT_FOUND(1048,"Image not found", HttpStatus.NOT_FOUND),
    IMAGE_LIMIT(1049, "Listing max image is 3", HttpStatus.BAD_REQUEST),
    LISTING_EXIST(1050, "Listing already exists", HttpStatus.BAD_REQUEST),
    BRAND_BLANK(1051, "Brand name cannot be blank", HttpStatus.BAD_REQUEST),
    DURATION_MAX(1052, "Duration max is 10", HttpStatus.BAD_REQUEST),
    DURATION_MIN(1053, "Duration min is 1", HttpStatus.BAD_REQUEST),
    DESCRIPTION_MAX(1054, "Description max is 1000", HttpStatus.BAD_REQUEST),
    FIELD_BLANK(1055, "Field cannot be blank", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1056, "Phone number is invalid", HttpStatus.BAD_REQUEST),
    ACCOUNT_INVALID(1057, "Account number must be 8-20 number", HttpStatus.BAD_REQUEST),
    ADDRESS_INVALID(1058, "Address must be 5-2000 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1059, "Email is invalid", HttpStatus.BAD_REQUEST),
    DURATION_DAY_MIN(1060, "Duration day min is 1", HttpStatus.BAD_REQUEST),
    PRIORITY_MIN(1061, "Priority min is 1", HttpStatus.BAD_REQUEST),
    ORDER_DELIVERD(1046,"Order must be deliverd", HttpStatus.BAD_REQUEST),
    BRAND_EXIST(1047, "Brand already exists", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_REGISTERED(1048,"User must register address to purchase", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1049,"Invalid key" , HttpStatus.BAD_REQUEST ),;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
