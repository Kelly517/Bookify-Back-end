package com.bookify.books.Bookify.exceptions.userexceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionResponse {

    private String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd 'T' HH:mm:ss")
    private LocalDateTime timestamp;
    private String exceptionMessage;
    private String exception;
}
