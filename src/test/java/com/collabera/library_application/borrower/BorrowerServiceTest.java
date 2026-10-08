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

import java.util.concurrent.atomic.AtomicLong;

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


        when(borrowerRepository.existsByEmailIgnoreCase(request.email()))
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
                .existsByEmailIgnoreCase(request.email());

        verify(borrowerRepository)
                .save(any(Borrower.class));
    }


    @Test
    void shouldAllowBorrowersWithSameNameAndDifferentEmails() {

        BorrowerRegistrationRequest firstRequest =
                new BorrowerRegistrationRequest("John Smith", "john@gmail.com");
        BorrowerRegistrationRequest secondRequest =
                new BorrowerRegistrationRequest("John Smith", "jane@gmail.com");
        AtomicLong idSequence = new AtomicLong();

        when(borrowerRepository.existsByEmailIgnoreCase(anyString()))
                .thenReturn(false);
        when(borrowerRepository.save(any(Borrower.class)))
                .thenAnswer(invocation -> {
                    Borrower borrower = invocation.getArgument(0);
                    borrower.setId(idSequence.incrementAndGet());
                    return borrower;
                });

        BorrowerResponse firstResponse = borrowerService.registerBorrower(firstRequest);
        BorrowerResponse secondResponse = borrowerService.registerBorrower(secondRequest);

        assertThat(firstResponse.name()).isEqualTo(secondResponse.name());
        assertThat(firstResponse.email()).isEqualTo("john@gmail.com");
        assertThat(secondResponse.email()).isEqualTo("jane@gmail.com");
        assertThat(firstResponse.id()).isNotEqualTo(secondResponse.id());
        verify(borrowerRepository).existsByEmailIgnoreCase("john@gmail.com");
        verify(borrowerRepository).existsByEmailIgnoreCase("jane@gmail.com");
        verify(borrowerRepository, times(2)).save(any(Borrower.class));
    }

    @Test
    void shouldThrowException_whenBorrowerEmailAlreadyExists() {

        BorrowerRegistrationRequest request =
                new BorrowerRegistrationRequest(
                        "John Smith",
                        "  JOHN@GMAIL.COM  "
                );


        when(borrowerRepository.existsByEmailIgnoreCase("john@gmail.com"))
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
                .existsByEmailIgnoreCase("john@gmail.com");

        verify(borrowerRepository, never())
                .save(any(Borrower.class));
    }

    @Test
    void shouldNormalizeEmailBeforeCheckingAndSaving() {
        BorrowerRegistrationRequest request =
                new BorrowerRegistrationRequest("John Smith", "  JOHN@GMAIL.COM  ");

        Borrower savedBorrower = Borrower.builder()
                .id(3L)
                .name(request.name())
                .email("john@gmail.com")
                .build();

        when(borrowerRepository.existsByEmailIgnoreCase("john@gmail.com"))
                .thenReturn(false);
        when(borrowerRepository.save(any(Borrower.class)))
                .thenReturn(savedBorrower);

        BorrowerResponse response = borrowerService.registerBorrower(request);

        assertThat(response.email()).isEqualTo("john@gmail.com");
        verify(borrowerRepository).existsByEmailIgnoreCase("john@gmail.com");
        verify(borrowerRepository).save(argThat(borrower ->
                borrower.getEmail().equals("john@gmail.com")));
    }

}
