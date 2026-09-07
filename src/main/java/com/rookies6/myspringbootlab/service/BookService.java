package com.rookies6.myspringbootlab.service;

import com.rookies6.myspringbootlab.controller.dto.BookDTO;
import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.exception.ErrorCode;
import com.rookies6.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(entity -> BookDTO.BookResponse.from(entity))
                .toList();
        //.collect(Collectors.toList());
    }

    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Book", "id", id));
        return BookDTO.BookResponse.from(book);
    }

    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Book", "isbn", isbn));
        return BookDTO.BookResponse.from(book);
    }

    public List<BookDTO.BookResponse> getBookByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);

        if(books.isEmpty()) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                    "Book", "author", author);
        }

        return books.stream()
                .map(entity -> BookDTO.BookResponse.from(entity))
                .toList();
    }

    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {

        bookRepository.findByIsbn(request.getIsbn())
                .ifPresent(b -> {
                    throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
                });

        Book book = request.toEntity();
        Book createdBook = bookRepository.save(book);
        return BookDTO.BookResponse.from(createdBook);

    }

    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Book", "id", id));

        // Update basic info
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        return BookDTO.BookResponse.from(book);
    }

    @Transactional
    public void deleteBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Book", "id", id));

        bookRepository.delete(book);
    }
}
