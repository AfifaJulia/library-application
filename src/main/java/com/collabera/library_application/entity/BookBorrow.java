package com.collabera.library_application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_borrows",
        indexes = {
        // Index to optimize queries for active borrows (not returned yet)
        @Index(
                name = "idx_active_book_borrow",
                columnList = "book_id, returned_at"
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "BookBorrow", description = "Represents a record of a book borrowed by a borrower")
public class BookBorrow {
    @Schema(description = "Primary key", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "The book that was borrowed", required = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    @Schema(description = "The borrower who borrowed the book", required = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borrower_id")
    private Borrower borrower;

    @Schema(description = "The date and time when the book was borrowed", example = "2024-06-01T10:15:30", required = true)
    @Column(nullable = false)
    private LocalDateTime borrowedAt;

    @Schema(description = "The date and time when the book was returned", example = "2024-06-15T14:30:00")
    private LocalDateTime returnedAt;
}
