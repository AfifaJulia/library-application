package com.collabera.library_application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "borrowers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Borrower", description = "Represents a Borrower of Library application")
public class Borrower extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @Schema(description = "borrower name", example = "afifa", required = true)
    private String name;

    @Column(nullable = false, unique = true)
    @Schema(description = "Unique borrower email", example = "afifa@example.com", required = true)
    private String email;

    @OneToMany(mappedBy = "borrower")
    private List<BookBorrow> borrowHistory = new ArrayList<>();
}
