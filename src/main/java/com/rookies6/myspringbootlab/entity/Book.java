package com.rookies6.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter @Setter
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
