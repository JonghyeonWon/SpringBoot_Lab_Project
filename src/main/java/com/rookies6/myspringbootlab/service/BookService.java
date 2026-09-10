package com.rookies6.myspringbootlab.service;

import com.rookies6.myspringbootlab.controller.dto.BookDTO;
import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.entity.BookDetail;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.exception.ErrorCode;
import com.rookies6.myspringbootlab.repository.BookDetailRepository;
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
    private final BookDetailRepository bookDetailRepository;

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(entity -> BookDTO.Response.fromEntity(entity))
                .toList();
        //.collect(Collectors.toList());
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Book", "id", id));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Book", "isbn", isbn));
        return BookDTO.Response.fromEntity(book);
    }

    public List<BookDTO.Response> getBookByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);

        if(books.isEmpty()) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                    "Book", "author", author);
        }

        return books.stream()
                .map(entity -> BookDTO.Response.fromEntity(entity))
                .toList();
    }

    public List<BookDTO.Response> getBookByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);

        if(books.isEmpty()) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                    "Book", "title", title);
        }

        return books.stream()
                .map(entity -> BookDTO.Response.fromEntity(entity))
                .toList();
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {

        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        BookDetail bookDetail = null;

        if (request.getDetailRequest() != null) {
            BookDTO.BookDetailDTO bookDetailDTO = request.getDetailRequest();
            bookDetail = BookDetail.builder()
                    .description(bookDetailDTO.getDescription())
                    .language(bookDetailDTO.getLanguage())
                    .pageCount(bookDetailDTO.getPageCount())
                    .publisher(bookDetailDTO.getPublisher())
                    .coverImageUrl(bookDetailDTO.getCoverImageUrl())
                    .edition(bookDetailDTO.getEdition())
                    .book(book)
                    .build();
        }

        if (bookDetail != null) {
            book.setBookDetail(bookDetail);
        }

        Book createdBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(createdBook);

    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {

        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        "Book", "id", id));

        if (!book.getIsbn().equals(request.getIsbn())) {
            if (bookRepository.existsByIsbn(request.getIsbn())) {
                throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
            }
            book.setIsbn(request.getIsbn());
        }

        // Update basic info
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        if (request.getDetailRequest() != null) {
            BookDTO.BookDetailDTO bookDetailDTO = request.getDetailRequest();

            if (book.getBookDetail() != null) {
                // 이미 상세 정보가 존재하는 경우: 필드만 업데이트
                BookDetail detail = book.getBookDetail();
                detail.setDescription(bookDetailDTO.getDescription());
                detail.setLanguage(bookDetailDTO.getLanguage());
                detail.setPageCount(bookDetailDTO.getPageCount());
                detail.setPublisher(bookDetailDTO.getPublisher());
                detail.setCoverImageUrl(bookDetailDTO.getCoverImageUrl());
                detail.setEdition(bookDetailDTO.getEdition());
            } else {
                BookDetail newDetail = BookDetail.builder()
                        .description(bookDetailDTO.getDescription())
                        .language(bookDetailDTO.getLanguage())
                        .pageCount(bookDetailDTO.getPageCount())
                        .publisher(bookDetailDTO.getPublisher())
                        .coverImageUrl(bookDetailDTO.getCoverImageUrl())
                        .edition(bookDetailDTO.getEdition())
                        .build();

                book.setBookDetail(newDetail);
            }
        }

        return BookDTO.Response.fromEntity(book);
    }

    @Transactional
    public void deleteBook(Long id) {

        if (!bookRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id);
        }

        bookRepository.deleteById(id);
    }
}
