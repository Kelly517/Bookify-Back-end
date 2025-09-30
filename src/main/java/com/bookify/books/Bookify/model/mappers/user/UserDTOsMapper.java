package com.bookify.books.Bookify.model.mappers.user;

import com.bookify.books.Bookify.model.dto.userdto.UserDTO;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.model.mappers.book.BookDTOsMapper;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Builder
public class UserDTOsMapper {

    // Convierte un User en una entidad UserDTO (para guardar en BD)
    public static UserDTO convertEntityToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .profilePhoto(user.getProfilePhoto())
                .name(user.getName())
                .lastname(user.getLastname())
                .userName(user.getUserName())
                .email(user.getEmail())
                .aboutMe(user.getAboutMe())
                .password(user.getPassword())
                .userRole(user.getUserRole())
                .purchasedBooks(user.getPurchasedBooks())
                .books(user.getBooks() != null ?
                        user.getBooks()
                                .stream()
                                .map(BookDTOsMapper::convertBookEntityToDTO)
                                .collect(Collectors.toList()) : null) //Este proceso interno es para buscar la lista de libros que ha creado el usuario
                .build();
    }

    // Convierte un UserDTO en una entidad UserDTO (para guardar en BD)
    public User convertDTOToEntity(UserDTO dto) {
        return User.builder()
                .userId(dto.getUserId())
                .profilePhoto(dto.getProfilePhoto())
                .name(dto.getName())
                .lastname(dto.getLastname())
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .aboutMe(dto.getAboutMe())
                .password(dto.getPassword())
                .userRole(dto.getUserRole())
                .purchasedBooks(dto.getPurchasedBooks())
                .books(dto.getBooks() != null ?
                        dto.getBooks()
                                .stream()
                                .map(BookDTOsMapper::convertBookDTOToEntity)
                                .collect(Collectors.toList()) : null) //Este proceso interno es para buscar la lista de libros que ha creado el usuario
                .build();
    }
}