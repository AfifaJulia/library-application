package com.collabera.library_application.book;

import com.collabera.library_application.book.dto.request.BookRegistrationRequest;
import com.collabera.library_application.book.dto.response.BookResponse;
import com.collabera.library_application.book.entity.Book;
import com.collabera.library_application.book.repository.BookRepository;
import com.collabera.library_application.book.service.impl.BookServiceImpl;
import com.collabera.library_application.enums.CustomErrors;
import com.collabera.library_application.exception.BookLibraryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldRegisterBookSuccessfully_whenBookDoesNotExist() {

        BookRegistrationRequest request =
                new BookRegistrationRequest(
                        "Clean Code",
                        "Robert C. Martin",
                        "9780132350884"
                );

        Book savedBook = Book.builder()
                .id(1L)
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .build();


        when(bookRepository.findFirstByIsbn(request.isbn()))
                .thenReturn(Optional.empty());

        when(bookRepository.save(any(Book.class)))
                .thenReturn(savedBook);


        BookResponse response = bookService.registerBook(request);


        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("Clean Code");
        assertThat(response.author()).isEqualTo("Robert C. Martin");
        assertThat(response.isbn()).isEqualTo("9780132350884");


        verify(bookRepository).findFirstByIsbn(request.isbn());
        verify(bookRepository).save(any(Book.class));
    }


    @Test
    void shouldThrowException_whenSameIsbnDifferentTitleOrAuthor() {

        BookRegistrationRequest request =
                new BookRegistrationRequest(
                        "Clean Architecture",
                        "Robert C. Martin",
                        "9780132350884"
                );


        Book existingBook = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .build();


        when(bookRepository.findFirstByIsbn(request.isbn()))
                .thenReturn(Optional.of(existingBook));


        BookLibraryException exception =
                assertThrows(
                        BookLibraryException.class,
                        () -> bookService.registerBook(request)
                );


        assertThat(exception.getMessage())
                .isEqualTo(CustomErrors.ISBN_ALREADY_EXISTS_WITH_DIFFERENT_TITLE_OR_AUTHOR.getErrorMessage());


        verify(bookRepository, never()).save(any(Book.class));
    }


    @Test
    void shouldRegisterBook_whenSameIsbnSameTitleAndAuthor() {

        BookRegistrationRequest request =
                new BookRegistrationRequest(
                        "9780132350884",
                        "Clean Code",
                        "Robert C. Martin"
                );


        Book existingBook = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .build();


        Book savedBook = Book.builder()
                .id(2L)
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .build();


        when(bookRepository.findFirstByIsbn(request.isbn()))
                .thenReturn(Optional.of(existingBook));

        when(bookRepository.save(any(Book.class)))
                .thenReturn(savedBook);


        BookResponse response =
                bookService.registerBook(request);


        assertThat(response.title())
                .isEqualTo("Clean Code");

        verify(bookRepository).save(any(Book.class));
    }


    @Test
    void shouldReturnAllBooks() {

        List<Book> books = List.of(
                Book.builder()
                        .id(1L)
                        .title("Clean Code")
                        .author("Robert C. Martin")
                        .isbn("9780132350884")
                        .build(),

                Book.builder()
                        .id(2L)
                        .title("Effective Java")
                        .author("Joshua Bloch")
                        .isbn("9780134685991")
                        .build()
        );


        when(bookRepository.findAll())
                .thenReturn(books);


        List<BookResponse> responses =
                bookService.getBooks();


        assertThat(responses)
                .hasSize(2);

        assertThat(responses.get(0).title())
                .isEqualTo("Clean Code");


        verify(bookRepository).findAll();
    }
}
