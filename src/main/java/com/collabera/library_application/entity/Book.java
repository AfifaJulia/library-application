package com.collabera.library_application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Book", description = "Represents a physical copy of Book of Library application")
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Book title", example = "The Great Gatsby", required = true)
    private String title;

    @Schema(description = "Book author", example = "F. Scott Fitzgerald", required = true)
    private String author;

    @Schema(description = "Book ISBN number", example = "978-0743273565", required = true)
    private String isbn;

    @OneToMany(mappedBy = "book")
    private List<BookBorrow> borrowHistory = new ArrayList<>();
}
