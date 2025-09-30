package com.bookify.books.Bookify.repository.userrepositories;

import com.bookify.books.Bookify.model.entities.userentities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRoleName(String roleName);
}