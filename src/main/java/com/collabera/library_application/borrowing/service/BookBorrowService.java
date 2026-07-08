package com.collabera.library_application.borrowing.service;

import com.collabera.library_application.borrowing.dto.request.BookBorrowRequest;
import com.collabera.library_application.borrowing.dto.request.BookReturnRequest;
import com.collabera.library_application.borrowing.dto.response.BookBorrowResponse;
import org.springframework.stereotype.Service;

@Service
public interface BookBorrowService {

    BookBorrowResponse borrowBook(BookBorrowRequest request);
    BookBorrowResponse returnBook(BookReturnRequest request);
}
