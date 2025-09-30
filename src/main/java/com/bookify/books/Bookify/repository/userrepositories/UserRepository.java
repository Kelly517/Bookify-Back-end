package com.bookify.books.Bookify.repository.userrepositories;

import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.bookify.books.Bookify.model.entities.userentities.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
/**
 * Se trae la tabla User para que se conecte a la base de datos
 * También se trae el tipo de dato del id para que las tablas coincidan
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);

    @Query("""
           SELECT books
           FROM User user
           JOIN user.purchasedBooks books
           WHERE user.userId = :userId
           AND books.isActive = true
           """)
    Page<BookEntity> findPurchasedBooksByUserId(@Param("userId") Long userId, Pageable pageable);
}
