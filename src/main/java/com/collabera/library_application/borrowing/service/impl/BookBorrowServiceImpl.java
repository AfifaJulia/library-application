package com.collabera.library_application.borrowing.service.impl;

import com.collabera.library_application.book.entity.Book;
import com.collabera.library_application.borrower.entity.Borrower;
import com.collabera.library_application.borrower.repository.BorrowerRepository;
import com.collabera.library_application.borrowing.dto.request.BookBorrowRequest;
import com.collabera.library_application.borrowing.dto.request.BookReturnRequest;
import com.collabera.library_application.borrowing.dto.response.BookBorrowResponse;
import com.collabera.library_application.borrowing.entity.BookBorrow;
import com.collabera.library_application.borrowing.mapper.BookBorrowMapper;
import com.collabera.library_application.borrowing.repository.BookBorrowRepository;
import com.collabera.library_application.borrowing.service.BookBorrowService;
import com.collabera.library_application.enums.CustomErrors;
import com.collabera.library_application.exception.BookLibraryException;
import com.collabera.library_application.book.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class BookBorrowServiceImpl implements BookBorrowService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private BookBorrowRepository bookBorrowRepository;


    @Override
    @Transactional
    public BookBorrowResponse borrowBook(BookBorrowRequest request) {

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new BookLibraryException(CustomErrors.BOOK_NOT_FOUND));

        Borrower borrower = borrowerRepository.findById(request.borrowerId())
                .orElseThrow(() -> new BookLibraryException(CustomErrors.BORROWER_NOT_FOUND));

        //Before creating a new BookBorrow, check whether there is already an active borrow for the book
        if (bookBorrowRepository.existsByBookIdAndReturnedAtIsNull(book.getId())) {
            throw new BookLibraryException(CustomErrors.BOOK_ALREADY_BORROWED);
        }

        BookBorrow bookBorrow = BookBorrow.builder()
                .book(book)
                .borrower(borrower)
                .borrowedAt(LocalDateTime.now())
                .build();

        try {

            BookBorrow savedBorrow = bookBorrowRepository.save(bookBorrow);

            log.info(
                    "Book {} borrowed successfully to {}",
                    savedBorrow.getBook().getTitle(),
                    savedBorrow.getBorrower().getName()
            );

            return BookBorrowMapper.toResponse(savedBorrow);

        } catch (DataIntegrityViolationException e) {

            if (e.getMessage().contains("uk_active_book_borrow")) {
                throw new BookLibraryException(
                        CustomErrors.BOOK_ALREADY_BORROWED
                );
            }

            throw e;
        }
    }

    @Override
    @Transactional
    public BookBorrowResponse returnBook(BookReturnRequest request) {
        //Find the active borrow for the book
        Optional<BookBorrow> activeBorrow = bookBorrowRepository.findByBookIdAndReturnedAtIsNull(request.bookId());

        if (!activeBorrow.isPresent()) {
            throw new BookLibraryException(CustomErrors.BOOK_NOT_BORROWED);
        }
        BookBorrow theBorrow = activeBorrow.get();

        theBorrow.setReturnedAt(LocalDateTime.now());
        log.info("Book {} is returned successfully.", theBorrow.getBook().getTitle());

        return BookBorrowMapper.toResponse(theBorrow);
    }
}
