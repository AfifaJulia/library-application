package com.collabera.library_application.borrowing.controller;

import com.collabera.library_application.borrowing.dto.request.BookBorrowRequest;
import com.collabera.library_application.borrowing.dto.request.BookReturnRequest;
import com.collabera.library_application.borrowing.dto.response.BookBorrowResponse;
import com.collabera.library_application.borrowing.service.BookBorrowService;
import com.collabera.library_application.constants.EndPoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Borrowing Controller",
        description = "Book borrowing and returning APIs"
)
public class BorrowingController {
    @Autowired
    public BookBorrowService bookBorrowService;

    @PostMapping(path=EndPoints.BOOK_BORROW)
    @Operation(
            summary = "Borrow Book",
            description = "Allows a borrower to borrow a book from the library"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request body containing the details of the book to be borrowed",
            required = true
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book or borrower not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BookBorrowResponse> borrowBook(@Valid @RequestBody BookBorrowRequest bookBorrowRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookBorrowService.borrowBook(bookBorrowRequest));
    }

    @PostMapping(path=EndPoints.BOOK_RETURN)
    @Operation(
            summary = "Return Book",
            description = "Allows a borrower to return a borrowed book to the library"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request body containing the details of the book to be returned",
            required = true
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book or borrower not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BookBorrowResponse> returnBook(@Valid @RequestBody BookReturnRequest bookReturnRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookBorrowService.returnBook(bookReturnRequest));
    }
}
