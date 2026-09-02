package com.rookies6.myspringbootlab.repository;

import com.rookies6.myspringbootlab.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
@Transactional
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {

        Book book = new Book();
        book.setTitle("JPA 프로그래밍");
        book.setAuthor("박둘리");
        book.setIsbn("9788956746432");
        book.setPrice(35000);
        book.setPublishDate(LocalDate.parse("2025-04-30"));

        bookRepository.save(book);
    }

    @Test
    public void testCreateBook() {

        Book book = new Book();
        book.setTitle("스프링 부트 입문");
        book.setAuthor("홍길동");
        book.setIsbn("9788956746425");
        book.setPrice(30000);
        book.setPublishDate(LocalDate.parse("2025-05-07"));

        Book resBook = bookRepository.save(book);

        assertThat(resBook.getIsbn()).isEqualTo("9788956746425");
    }

    @Test
    public void testFindByIsbn() {
        Book resBook = bookRepository.findByIsbn("9788956746432")
                .orElseThrow(() -> new NoSuchElementException("책을 찾을 수 없습니다."));

        assertThat(resBook.getIsbn()).isEqualTo("9788956746432");
    }

    @Test
    public void testFindByAuthor() {
        List<Book> books = bookRepository.findByAuthor("박둘리");

        assertThat(books)
                .isNotEmpty()
                .hasSize(1)
                .extracting(Book::getAuthor)
                .containsOnly("박둘리");
    }

    @Test
    public void testUpdateBook() {
        Book targetBook = bookRepository.findByIsbn("9788956746432")
                .orElseThrow(() -> new NoSuchElementException("책을 찾을 수 없습니다."));

        targetBook.setAuthor("김둘리");
        bookRepository.saveAndFlush(targetBook);    // 캐시에서 가져오기 방지용

        Book updatedBook = bookRepository.findByIsbn("9788956746432")
                .orElseThrow(() -> new NoSuchElementException("책을 찾을 수 없습니다."));

        assertThat(updatedBook.getAuthor()).isEqualTo("김둘리");
    }

    @Test
    public void testDeleteBook() {

        Book book = bookRepository.findByIsbn("9788956746432")
                .orElseThrow(() -> new NoSuchElementException("책을 찾을 수 없습니다."));

        bookRepository.delete(book);
        bookRepository.flush(); // 캐시에서 가져오기 방지용

        Optional<Book> deletedBook = bookRepository.findByIsbn("9788956746432");

        assertThat(deletedBook).isEmpty();
    }
}
