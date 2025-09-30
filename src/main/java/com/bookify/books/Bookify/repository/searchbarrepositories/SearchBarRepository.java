package com.bookify.books.Bookify.repository.searchbarrepositories;

import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchBarRepository extends JpaRepository<BookEntity, Long> {
    Page<BookEntity> findByTitleContainingIgnoreCaseAndIsActiveTrue(String title, Pageable pageable);
}
