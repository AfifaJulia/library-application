package com.collabera.library_application.borrower.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = EndPoints.BASE_URL+EndPoints.BORROWERS)
@Tag(
        name = "Borrower Controller",
        description = "Borrower registration and management APIs"
)
public class BorrowerController {

    @Autowired
    public BorrowerService borrowerService;

    @PostMapping
    @Operation(
            summary = "Create New Borrower",
            description = "Creates a new Borrower for the library application"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request body containing the details of the borrower to be registered",
            required = true
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Borrower registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BorrowerResponse> registerNewBorrower(@Valid @RequestBody BorrowerRegistrationRequest registerRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(borrowerService.registerBorrower(registerRequest));
    }
}
