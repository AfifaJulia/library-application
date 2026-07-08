package com.collabera.library_application.borrower.service;

import com.collabera.library_application.borrower.dto.request.BorrowerRegistrationRequest;
import com.collabera.library_application.borrower.dto.response.BorrowerResponse;
import org.springframework.stereotype.Service;

@Service
public interface BorrowerService {
    BorrowerResponse registerBorrower(BorrowerRegistrationRequest request);
}
