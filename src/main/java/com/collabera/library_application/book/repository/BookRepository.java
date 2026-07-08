package com.collabera.library_application.book.repository;

import com.collabera.library_application.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findFirstByIsbn(String isbn);
}
