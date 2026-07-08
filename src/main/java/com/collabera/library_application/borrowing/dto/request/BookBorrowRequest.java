package com.collabera.library_application.borrowing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request object for borrowing a book")
public record BookBorrowRequest(

    @NotNull(message = "Book ID is required")
    Long bookId,

    @NotNull(message = "Borrower ID is required")
    Long borrowerId
){

}
