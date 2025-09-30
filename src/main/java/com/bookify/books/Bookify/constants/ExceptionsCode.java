package com.bookify.books.Bookify.constants;

import lombok.Getter;

@Getter
public enum ExceptionsCode {

    ERROR_USER_EXISTS("UE100"),
    ERROR_INCORRECT_DATA_USER("UE101"),
    ERROR_USER_EMPTY("UE102"),

    ERROR_ROLE_EXISTS("RE300"),

    ERROR_BOOK_INVALID_ISBN("EB200"),
    ERROR_BOOK_DATA_EMPTY("EB201"),
    ERROR_BOOK_SIZE_DESCRIPTION("EB203"),
    ERROR_BOOK_ERXISTS("EB204"),

    ERROR_INVALID_EMAIL("EIE400"),
    ERROR_USER_EMAIL_NOT_EXISTS("ERE401"),

    GENERIC_CODE("GC500");

    private final String errorCode;

    ExceptionsCode(String errorCode) {
        this.errorCode = errorCode;
    }
}