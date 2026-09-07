package com.rookies6.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;

    @Column(unique = true)
    private String isbn;
    private Integer price;
    private LocalDate publishDate;
}
