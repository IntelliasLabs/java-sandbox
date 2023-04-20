package com.intellias.golden_example.repository.mysql.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = "id")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String title;
    private String author;
    private String isbn;

}
