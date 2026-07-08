package com.collabera.library_application.enums;


import org.springframework.http.HttpStatus;

public enum CustomErrors {
    BORROWER_NAME_ALREADY_EXISTS("Borrower name already exists",  HttpStatus.CONFLICT),
    BORROWER_EMAIL_ALREADY_EXISTS("Borrower email already exists", HttpStatus.CONFLICT),
    ISBN_ALREADY_EXISTS_WITH_DIFFERENT_TITLE_OR_AUTHOR("ISBN already exists with different title or author. Please check the details and try again.", HttpStatus.CONFLICT),
    BOOK_NOT_FOUND("Book not found", HttpStatus.NOT_FOUND),
    BOOK_ALREADY_BORROWED("Book is already borrowed", HttpStatus.CONFLICT),
    BORROWER_NOT_FOUND("Borrower not found", HttpStatus.NOT_FOUND),
    BORROWER_HAS_NO_BORROWED_BOOKS("Borrower has no borrowed books", HttpStatus.NOT_FOUND),
    BOOK_NOT_BORROWED("Invalid return request, book is not borrowed by the borrower", HttpStatus.BAD_REQUEST);

    private final String errorMessage;
    private final HttpStatus httpStatus;

    CustomErrors(String errorMessage, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

