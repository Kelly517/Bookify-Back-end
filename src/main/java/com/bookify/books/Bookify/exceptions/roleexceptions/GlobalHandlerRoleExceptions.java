package com.bookify.books.Bookify.exceptions.roleexceptions;

import com.bookify.books.Bookify.exceptions.mappercodes.MapperErrorCodesRole;
import com.bookify.books.Bookify.exceptions.userexceptions.ExceptionResponse;
import com.bookify.books.Bookify.constants.ExceptionsCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerRoleExceptions {

    @Autowired
    private MapperErrorCodesRole mapperErrorCodesRole;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> roleHandleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorCode = mapperErrorCodesRole.mapErrorsRoleCode(errors);

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                errorCode,
                LocalDateTime.now(),
                errors.toString(),
                methodArgumentNotValidException.toString()
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleExistsException.class)
    public ResponseEntity<Object> roleExistsException(RoleExistsException roleExistsException) {
        String errorCode = ExceptionsCode.ERROR_ROLE_EXISTS.getErrorCode();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        roleExistsException.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        int maxLinesOfException = 15;
        String limitedStackTraceExceptionLines = Arrays.stream(stackTrace.split(("n")))
                .limit(maxLinesOfException)
                .collect(Collectors.joining("\n"));

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                errorCode,
                LocalDateTime.now(),
                roleExistsException.getMessage(),
                limitedStackTraceExceptionLines
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }
}
