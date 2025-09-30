package com.bookify.books.Bookify.security;

import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    /**
     * Esta clase es la que permite cargar la información del usuario desde la base de datos una vez
     * que se ha validado su existencia y su veracidad con la autorización del token de seguridad.
     *
     */

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email"));

        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(user.getUserRole().getRoleName()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
