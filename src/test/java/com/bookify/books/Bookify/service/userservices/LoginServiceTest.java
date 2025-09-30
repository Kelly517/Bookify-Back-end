package com.bookify.books.Bookify.service.userservices;

import com.bookify.books.Bookify.exceptions.userexceptions.UserExistsException;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.model.entities.userentities.UserRole;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import com.bookify.books.Bookify.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;
    private static final Logger LOG = LoggerFactory.getLogger(LoginServiceTest.class);

    private LoginService loginService;

    Long userId = 1L;
    String email = "test@gmail.com";
    String password = "Password123*";
    String encodedPassword = "encoded1234";
    UserRole userRole = new UserRole(1L, "USER", List.of());


    @BeforeEach
    void setUp(){
        loginService = new LoginService(userRepository, passwordEncoder, jwtUtil, authenticationManager);
    }

    @Test
    void login() {
        User mock = new User();
        mock.setUserId(userId);
        mock.setEmail(email);
        mock.setPassword(encodedPassword);
        mock.setUserRole(userRole);

        Authentication authentication = mock(Authentication.class);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        doReturn(authorities).when(authentication).getAuthorities();
        when(jwtUtil.generateToken(userId, email, userRole.getRoleName())).thenReturn("jwt-token");

        String token = loginService.login(email, password);

        assertEquals("jwt-token", token);
    }

    @Test
    void shouldThrowException_whenEmailNotFound() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserExistsException exception = assertThrows(
                UserExistsException.class,
                () -> loginService.login(email, password),
                "User not found"
        );

        assertEquals("User not found", exception.getMessage());
    }
}