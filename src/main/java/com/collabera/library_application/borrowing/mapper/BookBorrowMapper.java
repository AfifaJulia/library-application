package com.collabera.library_application.borrowing.mapper;

import com.collabera.library_application.borrowing.dto.response.BookBorrowResponse;
import com.collabera.library_application.borrowing.entity.BookBorrow;
import org.springframework.stereotype.Component;

@Component
public class BookBorrowMapper {


    public static BookBorrowResponse toResponse(BookBorrow borrow) {

        return new BookBorrowResponse(
                borrow.getId(),
                borrow.getBook().getId(),
                borrow.getBorrower().getId(),
                borrow.getBorrowedAt(),
                borrow.getReturnedAt()
        );
    }
}
