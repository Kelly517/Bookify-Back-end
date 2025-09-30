package com.bookify.books.Bookify.exceptions.mappercodes;

import com.bookify.books.Bookify.constants.ExceptionsCode;

import java.util.Map;

public class MapperErrorCodeEmail {

    public String mapperErrorEmailCode(Map<String, String> errors) {
        for (String errorMessage : errors.values()) {
            if (errorMessage.contains("invalid")) {
                return ExceptionsCode.ERROR_INVALID_EMAIL.getErrorCode();
            }

            if (errorMessage.contains("exists")) {
                return ExceptionsCode.ERROR_USER_EMAIL_NOT_EXISTS.getErrorCode();
            }
        }
        return ExceptionsCode.GENERIC_CODE.getErrorCode();
    }
}
