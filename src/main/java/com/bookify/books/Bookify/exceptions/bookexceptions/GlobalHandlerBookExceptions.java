package com.bookify.books.Bookify.exceptions.bookexceptions;

import com.bookify.books.Bookify.exceptions.mappercodes.MapperErrorCodesBook;
import com.bookify.books.Bookify.exceptions.userexceptions.ExceptionResponse;
import com.bookify.books.Bookify.constants.ExceptionsCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequiredArgsConstructor
public class GlobalHandlerBookExceptions {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalHandlerBookExceptions.class);
    private final MapperErrorCodesBook mapperErrorCodesBook;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> bookHandleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorCodeBook = mapperErrorCodesBook.mapsErrorBookCode(errors);

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                errorCodeBook,
                LocalDateTime.now(),
                errors.toString(),
                methodArgumentNotValidException.getMessage()
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookExistsException.class)
    public ResponseEntity<Object> bookExistsException(BookExistsException bookExistsException) {
        String errorCode = ExceptionsCode.ERROR_ROLE_EXISTS.getErrorCode();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        bookExistsException.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        int maxLinesOfException = 15;
        String limitedStackTraceExceptionLines = Arrays.stream(stackTrace.split(("n")))
                .limit(maxLinesOfException)
                .collect(Collectors.joining("\n"));

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                errorCode,
                LocalDateTime.now(),
                bookExistsException.getMessage(),
                limitedStackTraceExceptionLines
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookAlreadyexistsException.class)
    public ResponseEntity<Object> bookAlreadyExistsException(BookAlreadyexistsException bookAlreadyexistsException) {
        String errorCode = ExceptionsCode.ERROR_ROLE_EXISTS.getErrorCode();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        bookAlreadyexistsException.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        int maxLinesOfException = 15;
        String limitedStackTraceExceptionLines = Arrays.stream(stackTrace.split(("n")))
                .limit(maxLinesOfException)
                .collect(Collectors.joining("\n"));

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                errorCode,
                LocalDateTime.now(),
                bookAlreadyexistsException.getMessage(),
                limitedStackTraceExceptionLines
        );
        LOG.error("Error criating book. Title already exists, {}", bookAlreadyexistsException.getMessage(), bookAlreadyexistsException);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }
}
