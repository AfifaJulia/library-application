package com.collabera.library_application.book.controller;

import com.collabera.library_application.book.dto.request.BookRegistrationRequest;
import com.collabera.library_application.book.dto.response.BookResponse;
import com.collabera.library_application.book.service.BookService;
import com.collabera.library_application.borrower.dto.request.BorrowerRegistrationRequest;
import com.collabera.library_application.borrower.dto.response.BorrowerResponse;
import com.collabera.library_application.borrower.service.BorrowerService;
import com.collabera.library_application.constants.EndPoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = EndPoints.BASE_URL+EndPoints.BOOKS)
@Tag(
        name = "Book Controller",
        description = "Book registration and management APIs"
)
public class BookController {

    @Autowired
    public BookService bookService;

    @Operation(
            summary = "Create New Book",
            description = "Creates a new Book for the library application"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request body containing the details of the book to be registered",
            required = true
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<BookResponse> registerNewBook(@Valid @RequestBody BookRegistrationRequest registerRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookService.registerBook(registerRequest));
    }

    @Operation(
            summary = "Get All Books",
            description = "Retrieves a list of all books in the library application"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request body containing the details of the book to be registered",
            required = true
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<BookResponse> getBookss() {
        return bookService.getBooks();
    }


}
