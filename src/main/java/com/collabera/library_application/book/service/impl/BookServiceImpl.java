package com.collabera.library_application.book.service.impl;
import com.collabera.library_application.book.dto.request.BookRegistrationRequest;
import com.collabera.library_application.book.dto.response.BookResponse;
import com.collabera.library_application.book.entity.Book;
import com.collabera.library_application.book.mapper.BookMapper;
import com.collabera.library_application.book.repository.BookRepository;
import com.collabera.library_application.enums.CustomErrors;
import com.collabera.library_application.exception.BookLibraryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collabera.library_application.book.service.BookService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;


    @Override
    @Transactional
    public BookResponse registerBook(BookRegistrationRequest request) {
        log.info("New Book Registration: {}", request);

        Optional<Book> existingBook =
                bookRepository.findFirstByIsbn(request.isbn());

        if (existingBook.isPresent()) {

            Book book = existingBook.get();

            if (!book.getTitle().equals(request.title())
                    || !book.getAuthor().equals(request.author())) {

                throw new BookLibraryException(CustomErrors.ISBN_ALREADY_EXISTS_WITH_DIFFERENT_TITLE_OR_AUTHOR);
            }
        }

        Book book = Book.builder()
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .build();
        book = bookRepository.save(book);
        log.info("Book Registration is done for {}", book.getTitle());
        return BookMapper.toResponse(book);
    }

    @Override
    public List<BookResponse> getBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }
}
