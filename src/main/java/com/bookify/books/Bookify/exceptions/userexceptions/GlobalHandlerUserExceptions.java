package com.bookify.books.Bookify.exceptions.userexceptions;

import com.bookify.books.Bookify.constants.ExceptionsCode;
import com.bookify.books.Bookify.exceptions.mappercodes.MapperErrorCodesUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerUserExceptions {

    @Autowired
    private MapperErrorCodesUser mapperErrorCodesUSer;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorCode = mapperErrorCodesUSer.mapErrorsCode(errors);

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                errorCode,
                LocalDateTime.now(),
                errors.toString(),
                methodArgumentNotValidException.getMessage()
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Object> userExistsException(UserExistsException userExistsException) {
        String errorMessage = userExistsException.getMessage();
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ExceptionsCode.ERROR_USER_EXISTS.getErrorCode(),
                LocalDateTime.now(),
                errorMessage,
                "The user already exists in the system."
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }
}
