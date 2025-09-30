package com.bookify.books.Bookify.repository.bookrepositories;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository  extends JpaRepository<BookEntity, Long> {
    Page<BookEntity> findAllByIsActiveTrue(Pageable pageable);
    Optional<BookEntity> findByBookIdentifierCodeAndIsActiveTrue(String findByBookIdentifierCode);
    Optional<BookEntity> findByTitleAndIsActiveTrue(String title);
    List<BookEntity> findAllByBookIdAndIsActiveTrue(Long bookId);
    List<BookEntity> findAllByBookIdInAndIsActiveTrue(List<Long> bookId);
    Page<BookEntity> findByCategoryContainingIgnoreCaseAndIsActiveTrue(String category, Pageable pageable);
}
