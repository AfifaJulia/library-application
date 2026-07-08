package com.collabera.library_application.borrowing.dto.response;

import java.time.LocalDateTime;

public record BookBorrowResponse(
        Long id,

        Long bookId,

        Long borrowerId,

        LocalDateTime borrowedAt,

        LocalDateTime returnedAt
) {
}
