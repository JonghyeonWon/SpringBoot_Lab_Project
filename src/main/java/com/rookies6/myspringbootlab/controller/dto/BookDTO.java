package com.rookies6.myspringbootlab.controller.dto;

import com.rookies6.myspringbootlab.entity.Book;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;


public class BookDTO {

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookCreateRequest {

        @NotBlank(message = "title is mandatory")
        private String title;

        @NotBlank(message = "author is mandatory")
        private String author;

        @NotBlank(message = "isbn is mandatory")
        private String isbn;

        @NotNull(message = "price is mandatory")
        @Min(value = 0, message = "price should be bigger than 0")
        private Integer price;

        private LocalDate publishDate;

        public Book toEntity() {
            return Book.builder()
                    .title(this.title)
                    .author(this.author)
                    .isbn(this.isbn)
                    .price(this.price)
                    .publishDate(this.publishDate)
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookUpdateRequest {

        @NotBlank(message = "title is mandatory")
        private String title;

        @NotBlank(message = "author is mandatory")
        private String author;

        @NotNull(message = "price is mandatory")
        @Min(value = 0, message = "price should be bigger than 0")
        private Integer price;

        private LocalDate publishDate;

    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookResponse {

        private Long id;

        @NotBlank(message = "title is mandatory")
        private String title;

        @NotBlank(message = "author is mandatory")
        private String author;

        @NotBlank(message = "isbn is mandatory")
        private String isbn;

        @NotNull(message = "price is mandatory")
        @Min(value = 0, message = "price should be bigger than 0")
        private Integer price;

        private LocalDate publishDate;

        public static BookResponse from(Book book) {
            return BookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .build();
        }
    }
}
