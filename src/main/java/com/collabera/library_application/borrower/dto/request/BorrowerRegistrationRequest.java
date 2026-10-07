package com.collabera.library_application.borrower.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request object for registering a borrower")
public record BorrowerRegistrationRequest(

    @NotBlank(message = "Borrower name is required")
    String name,

    @NotBlank(message = "Borrower email is required")
    @Email
    String email
){

}
