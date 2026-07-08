package com.collabera.library_application.borrower.service.impl;

import com.collabera.library_application.borrower.dto.request.BorrowerRegistrationRequest;
import com.collabera.library_application.borrower.dto.response.BorrowerResponse;
import com.collabera.library_application.borrower.entity.Borrower;
import com.collabera.library_application.borrower.mapper.BorrowerMapper;
import com.collabera.library_application.borrower.repository.BorrowerRepository;
import com.collabera.library_application.borrower.service.BorrowerService;
import com.collabera.library_application.enums.CustomErrors;
import com.collabera.library_application.exception.BookLibraryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class BorrowerServiceImpl implements BorrowerService {

    @Autowired
    private BorrowerRepository borrowerRepository;


    @Override
    @Transactional
    public BorrowerResponse registerBorrower(BorrowerRegistrationRequest request) {
        log.info("New Book Borrower Registration: {}", request);

        // 1. Check name
        if (borrowerRepository.existsByName(request.name())) {
            throw new BookLibraryException(CustomErrors.BORROWER_NAME_ALREADY_EXISTS);
        }

        // 2. Check email
        if (borrowerRepository.existsByEmail(request.email())) {
            throw new BookLibraryException(CustomErrors.BORROWER_EMAIL_ALREADY_EXISTS);
        }

        // Create and save the new borrower
        Borrower borrower = Borrower.builder()
                .name(request.name())
                .email(request.email())
                .build();
        borrower = borrowerRepository.save(borrower);

        log.info("Book Borrower Registration is done for {}", borrower.getName());
        return BorrowerMapper.toRegistrationResponse(borrower);

    }
}
