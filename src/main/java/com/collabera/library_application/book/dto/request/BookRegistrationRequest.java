package com.collabera.library_application.book.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request object for borrowing a book")
public record BookRegistrationRequest(

    @NotBlank(message = "ISBN number is required")
    String isbn,

    @NotBlank(message = "Book Title ID is required")
    String title,

    @NotBlank(message = "Book Author ID is required")
    String author
){

}
