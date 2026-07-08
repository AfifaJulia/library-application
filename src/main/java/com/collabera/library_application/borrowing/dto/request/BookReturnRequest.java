package com.collabera.library_application.borrowing.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookReturnRequest(
        @NotNull(message = "Book ID is required")
        Long bookId) {
}
