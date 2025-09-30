package com.bookify.books.Bookify.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {
/*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalHandlerException(Exception exception) {
        String errorGlobalException = ExceptionsCode.GENERIC_CODE.getErrorCode();
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .code(errorGlobalException)
                .timestamp(LocalDateTime.now())
                .exceptionMessage("Internal server error")
                .exception(Arrays.toString(exception.getStackTrace()))
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
}
