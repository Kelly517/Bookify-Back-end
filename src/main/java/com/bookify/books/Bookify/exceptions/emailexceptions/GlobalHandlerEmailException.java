package com.bookify.books.Bookify.exceptions.emailexceptions;

import com.bookify.books.Bookify.exceptions.userexceptions.ExceptionResponse;
import com.bookify.books.Bookify.constants.ExceptionsCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerEmailException {

    @ExceptionHandler(EmailSenderException.class)
    public ResponseEntity<Object> invalidEmailException(EmailSenderException emailSenderException) {
        String errorCode = ExceptionsCode.ERROR_ROLE_EXISTS.getErrorCode();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        emailSenderException.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();

        int maxLinesOfException = 6;
        String limitedStackTraceExceptionLines = Arrays.stream(stackTrace.split(("n")))
                .limit(maxLinesOfException)
                .collect(Collectors.joining("\n"));

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                errorCode,
                LocalDateTime.now(),
                emailSenderException.getMessage(),
                limitedStackTraceExceptionLines
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }
}
