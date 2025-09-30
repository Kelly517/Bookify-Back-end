package com.bookify.books.Bookify.shared.interfaces.securityinterfaces;

import java.security.SecureRandom;

// contrato para generar códigos únicos

public interface GenerateUniqueCode {
    // Metodo por defecto que genera un codigo alfanumérico aleatorio de 6 caracteres
    default String generateUniqueId() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();// generador seguro de números aleatorios

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {// se repite 6 veces para crear el código
            int index = random.nextInt(CHARACTERS.length());// elige una posición aleatoria
            stringBuilder.append(CHARACTERS.charAt(index)); // agrega el carácter al código
        }
        return stringBuilder.toString(); // devuelve el código generado
    }
}
