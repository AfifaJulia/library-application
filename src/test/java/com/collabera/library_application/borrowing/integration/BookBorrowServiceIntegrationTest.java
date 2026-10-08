package com.collabera.library_application.borrowing.integration;

import com.collabera.library_application.book.entity.Book;
import com.collabera.library_application.book.repository.BookRepository;
import com.collabera.library_application.borrower.entity.Borrower;
import com.collabera.library_application.borrower.repository.BorrowerRepository;
import com.collabera.library_application.borrowing.dto.request.BookBorrowRequest;
import com.collabera.library_application.borrowing.dto.response.BookBorrowResponse;
import com.collabera.library_application.borrowing.repository.BookBorrowRepository;
import com.collabera.library_application.borrowing.service.BookBorrowService;
import com.collabera.library_application.enums.CustomErrors;
import com.collabera.library_application.exception.BookLibraryException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookBorrowServiceIntegrationTest {

    @Autowired
    private BookBorrowService bookBorrowService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private BookBorrowRepository bookBorrowRepository;

    @Test
    @Transactional
    void shouldBorrowBookSuccessfully() {
        Book book = bookRepository.save(Book.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .build());
        Borrower borrower = borrowerRepository.save(Borrower.builder()
                .name("Integration Test Borrower")
                .email("borrower-" + java.util.UUID.randomUUID() + "@example.com")
                .build());

        BookBorrowResponse response = bookBorrowService.borrowBook(
                new BookBorrowRequest(book.getId(), borrower.getId()));

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.bookId()).isEqualTo(book.getId());
        assertThat(response.borrowerId()).isEqualTo(borrower.getId());
        assertThat(bookBorrowRepository.findByBookIdAndReturnedAtIsNull(book.getId()))
                .isPresent();
    }

    @Test
    void concurrentBorrowRequestsAllowOnlyOneBorrower() throws Exception {
        Book book = bookRepository.saveAndFlush(Book.builder()
                .title("Concurrent Borrow Test")
                .author("Integration Test")
                .isbn("isbn-" + java.util.UUID.randomUUID())
                .build());
        Borrower firstBorrower = createBorrower();
        Borrower secondBorrower = createBorrower();

        // Hold both requests until their worker threads are ready, then release them together.
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        // Record results safely because both worker threads update the counters.
        AtomicInteger successfulBorrows = new AtomicInteger();
        AtomicInteger rejectedBorrows = new AtomicInteger();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            // Futures let the test wait for both attempts and surface worker-thread failures.
            Future<?> firstAttempt = executor.submit(() -> attemptBorrow(
                    book.getId(), firstBorrower.getId(), ready, start,
                    successfulBorrows, rejectedBorrows));
            Future<?> secondAttempt = executor.submit(() -> attemptBorrow(
                    book.getId(), secondBorrower.getId(), ready, start,
                    successfulBorrows, rejectedBorrows));

            // Wait for both threads to be ready, then release them simultaneously.
            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();

            // Release both threads to attempt borrowing the same book at the same time.
            start.countDown();
            //why not ready.countDown()?
            // Because ready is used to signal that both threads are ready, while start is used to signal that they can start attempting to borrow the book.
            // We want to wait until both threads are ready before allowing them to proceed.

            // Wait for both attempts to complete, with a timeout to avoid hanging the test indefinitely.
            firstAttempt.get(10, TimeUnit.SECONDS);
            secondAttempt.get(10, TimeUnit.SECONDS);
        } finally {
            // Ensure that the executor is shut down and that all tasks have completed before finishing the test.
            start.countDown();
            executor.shutdownNow();
            // Wait for the executor to terminate, with a timeout to avoid hanging the test indefinitely.
            assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();
        }

        assertThat(successfulBorrows.get()).isEqualTo(1);
        assertThat(rejectedBorrows.get()).isEqualTo(1);
        // Verify that the book is indeed borrowed and not available for borrowing again.
        assertThat(bookBorrowRepository.findByBookIdAndReturnedAtIsNull(book.getId()))
                .isPresent();
    }

    private Borrower createBorrower() {
        return borrowerRepository.saveAndFlush(Borrower.builder()
                .name("Concurrent Test Borrower")
                .email("borrower-" + java.util.UUID.randomUUID() + "@example.com")
                .build());
    }

    private void attemptBorrow(
            Long bookId,
            Long borrowerId,
            CountDownLatch ready,
            CountDownLatch start,
            AtomicInteger successfulBorrows,
            AtomicInteger rejectedBorrows) {
        ready.countDown();
        try {
            if (!start.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Timed out waiting to start concurrent borrow");
            }

            bookBorrowService.borrowBook(new BookBorrowRequest(bookId, borrowerId));
            successfulBorrows.incrementAndGet();
        } catch (BookLibraryException exception) {
            assertThat(exception.getErrorMessage())
                    .isEqualTo(CustomErrors.BOOK_ALREADY_BORROWED);
            rejectedBorrows.incrementAndGet();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Concurrent borrow test was interrupted", exception);
        }
    }
}
