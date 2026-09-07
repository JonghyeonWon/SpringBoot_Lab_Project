package com.rookies6.myspringbootlab.controller;

import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.repository.BookRepository;
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
public class BookRestController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> targetBook = bookRepository.findById(id);
        Book findBook = targetBook.orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(findBook);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Optional<Book> targetBook = bookRepository.findByIsbn(isbn);
        Book findBook = targetBook.orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(findBook);
    }

    @GetMapping("/author/{author}")
    public List<Book> getBookByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookRepository.save(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Optional<Book> targetBook = bookRepository.findById(id);
        Book findBook = targetBook.orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        findBook.setTitle(book.getTitle());
        findBook.setAuthor(book.getAuthor());
        findBook.setIsbn(book.getIsbn());
        findBook.setPrice(book.getPrice());
        findBook.setPublishDate(book.getPublishDate());
        return ResponseEntity.ok(bookRepository.save(findBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Optional<Book> targetBook = bookRepository.findById(id);
        Book findBook = targetBook.orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        bookRepository.delete(findBook);
        return ResponseEntity.ok().build();
    }
}
