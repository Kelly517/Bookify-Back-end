package com.bookify.books.Bookify.service.bookservices;

import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.model.entities.userentities.UserRole;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import com.bookify.books.Bookify.service.userservices.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void userRegister() {
        User user = new User();
        UserRole userRole = new UserRole();
    }

    @Test
    void getUser() {
    }

    @Test
    void userDelete() {
    }

    @Test
    void userUpdate() {
    }
}