package com.collabera.library_application.borrower;

import com.collabera.library_application.borrower.dto.request.BorrowerRegistrationRequest;
import com.collabera.library_application.borrower.dto.response.BorrowerResponse;
import com.collabera.library_application.borrower.entity.Borrower;
import com.collabera.library_application.borrower.repository.BorrowerRepository;
import com.collabera.library_application.borrower.service.impl.BorrowerServiceImpl;
import com.collabera.library_application.enums.CustomErrors;
import com.collabera.library_application.exception.BookLibraryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BorrowerServiceImplTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldRegisterBorrowerSuccessfully() {

        BorrowerRegistrationRequest request =
                new BorrowerRegistrationRequest(
                        "John Smith",
                        "john@gmail.com"
                );


        Borrower savedBorrower = Borrower.builder()
                .id(1L)
                .name(request.name())
                .email(request.email())
                .build();


        when(borrowerRepository.existsByName(request.name()))
                .thenReturn(false);

        when(borrowerRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(borrowerRepository.save(any(Borrower.class)))
                .thenReturn(savedBorrower);


        BorrowerResponse response =
                borrowerService.registerBorrower(request);


        assertThat(response)
                .isNotNull();

        assertThat(response.name())
                .isEqualTo("John Smith");

        assertThat(response.email())
                .isEqualTo("john@gmail.com");


        verify(borrowerRepository)
                .existsByName(request.name());

        verify(borrowerRepository)
                .existsByEmail(request.email());

        verify(borrowerRepository)
                .save(any(Borrower.class));
    }


    @Test
    void shouldThrowException_whenBorrowerNameAlreadyExists() {

        BorrowerRegistrationRequest request =
                new BorrowerRegistrationRequest(
                        "John Smith",
                        "john@gmail.com"
                );


        when(borrowerRepository.existsByName(request.name()))
                .thenReturn(true);


        BookLibraryException exception =
                assertThrows(
                        BookLibraryException.class,
                        () -> borrowerService.registerBorrower(request)
                );


        assertThat(exception.getMessage())
                .isEqualTo(
                        CustomErrors.BORROWER_NAME_ALREADY_EXISTS.getErrorMessage()
                );


        verify(borrowerRepository)
                .existsByName(request.name());

        verify(borrowerRepository, never())
                .existsByEmail(anyString());

        verify(borrowerRepository, never())
                .save(any(Borrower.class));
    }


    @Test
    void shouldThrowException_whenBorrowerEmailAlreadyExists() {

        BorrowerRegistrationRequest request =
                new BorrowerRegistrationRequest(
                        "John Smith",
                        "john@gmail.com"
                );


        when(borrowerRepository.existsByName(request.name()))
                .thenReturn(false);

        when(borrowerRepository.existsByEmail(request.email()))
                .thenReturn(true);


        BookLibraryException exception =
                assertThrows(
                        BookLibraryException.class,
                        () -> borrowerService.registerBorrower(request)
                );


        assertThat(exception.getMessage())
                .isEqualTo(
                        CustomErrors.BORROWER_EMAIL_ALREADY_EXISTS.getErrorMessage()
                );


        verify(borrowerRepository)
                .existsByName(request.name());

        verify(borrowerRepository)
                .existsByEmail(request.email());

        verify(borrowerRepository, never())
                .save(any(Borrower.class));
    }
}
