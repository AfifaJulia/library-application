package com.collabera.library_application.borrowing.repository;

import com.collabera.library_application.borrowing.entity.BookBorrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookBorrowRepository extends JpaRepository<BookBorrow, Long> {

    Optional<BookBorrow> findByBookIdAndReturnedAtIsNull(Long bookId);

    boolean existsByBookIdAndReturnedAtIsNull(Long bookId);

    List<BookBorrow> findByBorrowerId(Long borrowerId);
}