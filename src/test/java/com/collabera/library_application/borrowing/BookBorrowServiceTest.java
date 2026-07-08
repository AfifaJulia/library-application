package com.collabera.library_application.borrowing;

import com.collabera.library_application.book.entity.Book;
import com.collabera.library_application.book.repository.BookRepository;
import com.collabera.library_application.borrower.entity.Borrower;
import com.collabera.library_application.borrower.repository.BorrowerRepository;
import com.collabera.library_application.borrowing.dto.request.BookBorrowRequest;
import com.collabera.library_application.borrowing.dto.request.BookReturnRequest;
import com.collabera.library_application.borrowing.dto.response.BookBorrowResponse;
import com.collabera.library_application.borrowing.entity.BookBorrow;
import com.collabera.library_application.borrowing.repository.BookBorrowRepository;
import com.collabera.library_application.borrowing.service.impl.BookBorrowServiceImpl;
import com.collabera.library_application.enums.CustomErrors;
import com.collabera.library_application.exception.BookLibraryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class BookBorrowServiceTest {

    @Mock
    private BookBorrowRepository bookBorrowRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BookBorrowServiceImpl bookBorrowService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldBorrowBookSuccessfully() {

        BookBorrowRequest request =
                new BookBorrowRequest(1L, 10L);


        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .build();


        Borrower borrower = Borrower.builder()
                .id(10L)
                .name("John")
                .email("john@gmail.com")
                .build();


        BookBorrow bookBorrow = BookBorrow.builder()
                .book(book)
                .borrower(borrower)
                .build();


        when(bookBorrowRepository.findByBookIdAndReturnedAtIsNull(1L))
                .thenReturn(Optional.empty());

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(borrowerRepository.findById(10L))
                .thenReturn(Optional.of(borrower));

        when(bookBorrowRepository.save(any(BookBorrow.class)))
                .thenReturn(bookBorrow);


        BookBorrowResponse response =
                bookBorrowService.borrowBook(request);


        assertThat(response)
                .isNotNull();


        verify(bookBorrowRepository)
                .findByBookIdAndReturnedAtIsNull(1L);

        verify(bookRepository)
                .findById(1L);

        verify(borrowerRepository)
                .findById(10L);

        verify(bookBorrowRepository)
                .save(any(BookBorrow.class));
    }


    @Test
    void shouldThrowException_whenBookAlreadyBorrowed() {

        BookBorrowRequest request =
                new BookBorrowRequest(1L, 10L);


        BookBorrow existingBorrow =
                BookBorrow.builder()
                        .build();


        when(bookBorrowRepository.findByBookIdAndReturnedAtIsNull(1L))
                .thenReturn(Optional.of(existingBorrow));


        BookLibraryException exception =
                assertThrows(
                        BookLibraryException.class,
                        () -> bookBorrowService.borrowBook(request)
                );


        assertThat(exception.getMessage())
                .isEqualTo(
                        CustomErrors.BOOK_ALREADY_BORROWED.getErrorMessage()
                );


        verify(bookRepository, never())
                .findById(anyLong());

        verify(borrowerRepository, never())
                .findById(anyLong());
    }


    @Test
    void shouldThrowException_whenBookNotFound() {

        BookBorrowRequest request =
                new BookBorrowRequest(1L, 10L);


        when(bookBorrowRepository.findByBookIdAndReturnedAtIsNull(1L))
                .thenReturn(Optional.empty());

        when(bookRepository.findById(1L))
                .thenReturn(Optional.empty());


        BookLibraryException exception =
                assertThrows(
                        BookLibraryException.class,
                        () -> bookBorrowService.borrowBook(request)
                );


        assertThat(exception.getMessage())
                .isEqualTo(
                        CustomErrors.BOOK_NOT_FOUND.getErrorMessage()
                );


        verify(bookBorrowRepository, never())
                .save(any());
    }


    @Test
    void shouldThrowException_whenBorrowerNotFound() {

        BookBorrowRequest request =
                new BookBorrowRequest(1L, 10L);


        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .build();


        when(bookBorrowRepository.findByBookIdAndReturnedAtIsNull(1L))
                .thenReturn(Optional.empty());

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(borrowerRepository.findById(10L))
                .thenReturn(Optional.empty());


        BookLibraryException exception =
                assertThrows(
                        BookLibraryException.class,
                        () -> bookBorrowService.borrowBook(request)
                );


        assertThat(exception.getMessage())
                .isEqualTo(
                        CustomErrors.BORROWER_NOT_FOUND.getErrorMessage()
                );


        verify(bookBorrowRepository, never())
                .save(any());
    }


    @Test
    void shouldReturnBookSuccessfully() {

        BookReturnRequest request =
                new BookReturnRequest(1L);


        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .build();


        Borrower borrower = Borrower.builder()
                .id(10L)
                .name("John")
                .build();


        BookBorrow activeBorrow =
                BookBorrow.builder()
                        .book(book)
                        .borrower(borrower)
                        .build();


        when(bookBorrowRepository.findByBookIdAndReturnedAtIsNull(1L))
                .thenReturn(Optional.of(activeBorrow));


        BookBorrowResponse response =
                bookBorrowService.returnBook(request);


        assertThat(response)
                .isNotNull();

        assertThat(activeBorrow.getReturnedAt())
                .isNotNull();


        verify(bookBorrowRepository)
                .findByBookIdAndReturnedAtIsNull(1L);
    }


    @Test
    void shouldThrowException_whenBookIsNotBorrowed() {

        BookReturnRequest request =
                new BookReturnRequest(1L);


        when(bookBorrowRepository.findByBookIdAndReturnedAtIsNull(1L))
                .thenReturn(Optional.empty());


        BookLibraryException exception =
                assertThrows(
                        BookLibraryException.class,
                        () -> bookBorrowService.returnBook(request)
                );


        assertThat(exception.getMessage())
                .isEqualTo(
                        CustomErrors.BOOK_NOT_BORROWED.getErrorMessage()
                );
    }
}