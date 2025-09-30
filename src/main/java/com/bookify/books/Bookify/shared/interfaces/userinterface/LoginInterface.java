package com.bookify.books.Bookify.shared.interfaces.userinterface;
// contrato para el inicio de sesión
public interface LoginInterface {

    // Realiza el login con el email y la contraseña
    // Devuelve un String un token JWT
    String login(String email, String password);
}
