package com.rookies6.myspringbootlab.repository;

import com.rookies6.myspringbootlab.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDetailRepository extends JpaRepository<BookDetail, Long> {

    public Optional<BookDetail> findByBookId(Long bookId);

    @Query("SELECT bd FROM BookDetail bd JOIN FETCH bd.book WHERE bd.id = :id")
    public Optional<BookDetail> findByIdWithBook(@Param("id") Long id);

    public List<BookDetail> findByPublisher(String publisher);
}
