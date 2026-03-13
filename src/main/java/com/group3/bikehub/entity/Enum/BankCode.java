package com.group3.bikehub.entity.Enum;

import lombok.Getter;

@Getter
public enum BankCode {
    TECHCOMBANK("970407"),
    VIETINBANK("970415"),
    MB_BANK("970422")
    ;
    private String code;
    BankCode(String code) {
        this.code = code;
    }
}
