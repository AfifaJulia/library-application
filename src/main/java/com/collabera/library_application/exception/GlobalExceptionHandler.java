package com.collabera.library_application.exception;


import com.collabera.library_application.pojo.BaseAPIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookLibraryException.class)
    public ResponseEntity<BaseAPIResponse> handleBookLibraryException(BookLibraryException ex) {

        return ResponseEntity
                .status(ex.getErrorMessage().getHttpStatus())
                .body(new BaseAPIResponse(
                        "ERROR",
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseAPIResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseAPIResponse(
                        "ERROR",
                        message
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseAPIResponse> handleGeneric(
            Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseAPIResponse(
                        "ERROR",
                        ex.getMessage()
                )
        );
    }
}