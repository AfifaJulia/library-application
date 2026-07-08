package com.collabera.library_application.book.controller;

import com.collabera.library_application.book.dto.request.BookRegistrationRequest;
import com.collabera.library_application.book.dto.response.BookResponse;
import com.collabera.library_application.book.service.BookService;
import com.collabera.library_application.borrower.dto.request.BorrowerRegistrationRequest;
import com.collabera.library_application.borrower.dto.response.BorrowerResponse;
import com.collabera.library_application.borrower.service.BorrowerService;
import com.collabera.library_application.constants.EndPoints;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = EndPoints.BASE_URL+EndPoints.BOOKS)
public class BookController {

    @Autowired
    public BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> registerNewBook(@Valid @RequestBody BookRegistrationRequest registerRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookService.registerBook(registerRequest));
    }
    @GetMapping
    public List<BookResponse> getBookss() {
        return bookService.getBooks();
    }


}
