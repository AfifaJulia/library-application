package com.collabera.library_application.book.dto.response;

import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String  title,
        String author,
        String isbn,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
