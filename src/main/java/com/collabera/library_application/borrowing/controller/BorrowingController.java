package com.collabera.library_application.borrowing.controller;

import com.collabera.library_application.borrowing.dto.request.BookBorrowRequest;
import com.collabera.library_application.borrowing.dto.request.BookReturnRequest;
import com.collabera.library_application.borrowing.dto.response.BookBorrowResponse;
import com.collabera.library_application.borrowing.service.BookBorrowService;
import com.collabera.library_application.constants.EndPoints;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = EndPoints.BASE_URL+EndPoints.BOOKS)
public class BorrowingController {
    @Autowired
    public BookBorrowService bookBorrowService;

    @PostMapping(path=EndPoints.BOOK_BORROW)
    public ResponseEntity<BookBorrowResponse> borrowBook(@Valid @RequestBody BookBorrowRequest bookBorrowRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookBorrowService.borrowBook(bookBorrowRequest));
    }

    @PostMapping(path=EndPoints.BOOK_RETURN)
    public ResponseEntity<BookBorrowResponse> returnBook(@Valid @RequestBody BookReturnRequest bookReturnRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookBorrowService.returnBook(bookReturnRequest));
    }
}
