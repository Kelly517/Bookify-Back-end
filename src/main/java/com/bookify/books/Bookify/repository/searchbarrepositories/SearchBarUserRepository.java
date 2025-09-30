package com.bookify.books.Bookify.repository.searchbarrepositories;

import com.bookify.books.Bookify.model.entities.userentities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchBarUserRepository extends JpaRepository<User, Long> {
    Page<User> findByUserNameContainingIgnoreCase(String userName, Pageable pageable);
}
