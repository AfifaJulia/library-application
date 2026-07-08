package com.collabera.library_application.borrower.dto.response;

import java.time.LocalDateTime;

public record BorrowerResponse(
        Long id,
        String name,
        String email
) {
}
