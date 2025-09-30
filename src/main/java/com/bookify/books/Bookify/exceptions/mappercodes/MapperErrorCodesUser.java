package com.bookify.books.Bookify.exceptions.mappercodes;

import com.bookify.books.Bookify.constants.ExceptionsCode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MapperErrorCodesUser {

    public String mapErrorsCode(Map<String, String> error) {
        for (String errorMessage : error.values()) {
            if (errorMessage.contains("Invalid")) {
                return ExceptionsCode.ERROR_INCORRECT_DATA_USER.getErrorCode();
            }

            if (errorMessage.contains("empty")) {
                return ExceptionsCode.ERROR_USER_EMPTY.getErrorCode();
            }
        }

        return ExceptionsCode.GENERIC_CODE.getErrorCode();
    }
}
