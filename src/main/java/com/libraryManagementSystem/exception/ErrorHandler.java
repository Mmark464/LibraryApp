package com.libraryManagementSystem.exception;

import com.libraryManagementSystem.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException ex) {

        int status = HttpStatus.NOT_FOUND.value();
        return ResponseEntity.status(status)
                .body(ErrorDto.builder()
                        .status(status)
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {

        int status = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(status).body(ErrorDto.builder()
                .status(status)
                .message(ex.getMessage())
                .build());
    }

}
