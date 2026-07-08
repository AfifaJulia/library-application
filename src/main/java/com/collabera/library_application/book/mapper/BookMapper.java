package com.collabera.library_application.book.mapper;

import com.collabera.library_application.book.dto.response.BookResponse;
import com.collabera.library_application.book.entity.Book;

public class BookMapper {
    public static BookResponse toResponse(Book book) {

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}
