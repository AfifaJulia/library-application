package com.collabera.library_application.book.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request object for borrowing a book")
public record BookRegistrationRequest(

    @NotNull(message = "ISBN number is required")
    String isbn,

    @NotNull(message = "Book Title ID is required")
    String title,

    @NotNull(message = "Book Author ID is required")
    String author
){

}
