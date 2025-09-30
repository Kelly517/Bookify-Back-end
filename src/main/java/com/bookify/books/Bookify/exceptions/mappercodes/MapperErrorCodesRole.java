package com.bookify.books.Bookify.exceptions.mappercodes;

import com.bookify.books.Bookify.constants.ExceptionsCode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MapperErrorCodesRole {

    public String mapErrorsRoleCode(Map<String, String> errors) {
        for (String errorMessage : errors.values()) {
            if (errorMessage.contains("exists")) {
                return ExceptionsCode.ERROR_ROLE_EXISTS.getErrorCode();
            }
        }
        return ExceptionsCode.GENERIC_CODE.getErrorCode();
    }
}
