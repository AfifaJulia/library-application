package com.collabera.library_application.borrower.controller;

import com.collabera.library_application.borrower.dto.request.BorrowerRegistrationRequest;
import com.collabera.library_application.borrower.dto.response.BorrowerResponse;
import com.collabera.library_application.borrower.service.BorrowerService;
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
@RequestMapping(value = EndPoints.BASE_URL+EndPoints.BORROWERS)
public class BorrowerController {

    @Autowired
    public BorrowerService borrowerService;

    @PostMapping
    public ResponseEntity<BorrowerResponse> registerNewBorrower(@Valid @RequestBody BorrowerRegistrationRequest registerRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(borrowerService.registerBorrower(registerRequest));
    }
}
