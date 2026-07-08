package com.collabera.library_application.borrower.repository;

import com.collabera.library_application.borrower.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    boolean existsByName(String borrowerName);
    boolean existsByEmail(String borrowerEmail);
}
