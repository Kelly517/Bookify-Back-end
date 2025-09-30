package com.bookify.books.Bookify.exceptions.mappercodes;

import com.bookify.books.Bookify.constants.ExceptionsCode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MapperErrorCodesBook {

    public String mapsErrorBookCode(Map<String, String> errors) {
        for (String errorMessage : errors.values()) {
            if (errorMessage.contains("ISBN")) {
                return ExceptionsCode.ERROR_BOOK_INVALID_ISBN.getErrorCode();
            }

            if (errorMessage.contains("empty")) {
                return ExceptionsCode.ERROR_BOOK_DATA_EMPTY.getErrorCode();
            }

            if (errorMessage.contains("longer")) {
                return ExceptionsCode.ERROR_BOOK_SIZE_DESCRIPTION.getErrorCode();
            }

            if (errorMessage.contains("exists")) {
                return ExceptionsCode.ERROR_BOOK_ERXISTS.getErrorCode();
            }
        }
        return ExceptionsCode.GENERIC_CODE.getErrorCode();
    }
}
