package com.collabera.library_application.exception;

import com.collabera.library_application.enums.CustomErrors;

public class BookLibraryException extends RuntimeException {

    private final CustomErrors errors;

    public BookLibraryException(String errorMessage, Throwable cause, CustomErrors errors) {
        super(errorMessage, cause);
        this.errors = errors;
    }

    public BookLibraryException(CustomErrors errors) {
        super(errors.getErrorMessage());
        this.errors = errors;
    }

    public CustomErrors getErrorMessage() {
        return errors;
    }

}