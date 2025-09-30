package com.bookify.books.Bookify.components;

import com.bookify.books.Bookify.model.dto.userdto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse {
    private UserDTO userDTO;
    private String token;
}
