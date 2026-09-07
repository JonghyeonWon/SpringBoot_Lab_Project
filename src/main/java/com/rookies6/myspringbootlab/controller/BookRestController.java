package com.rookies6.myspringbootlab.controller;

import com.rookies6.myspringbootlab.controller.dto.BookDTO;
import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.repository.BookRepository;
import com.rookies6.myspringbootlab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO.BookResponse>> getAllBooks() {
        List<BookDTO.BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> getBookById(@PathVariable Long id) {
        BookDTO.BookResponse targetBook = bookService.getBookById(id);
        return ResponseEntity.ok(targetBook);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.BookResponse> getBookByIsbn(@PathVariable String isbn) {
        BookDTO.BookResponse targetBook = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(targetBook);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDTO.BookResponse>> getBookByAuthor(@PathVariable String author) {
        List<BookDTO.BookResponse> targetBooks = bookService.getBookByAuthor(author);
        return ResponseEntity.ok(targetBooks);
    }

    @PostMapping
    public ResponseEntity<BookDTO.BookResponse> createBook(@Valid @RequestBody BookDTO.BookCreateRequest request) {
        BookDTO.BookResponse createdBook = bookService.createBook(request);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO.BookUpdateRequest request) {
        BookDTO.BookResponse updatedBook = bookService.updateBook(id, request);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
