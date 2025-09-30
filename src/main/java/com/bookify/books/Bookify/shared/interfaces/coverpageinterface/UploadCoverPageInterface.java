package com.bookify.books.Bookify.shared.interfaces.coverpageinterface;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadCoverPageInterface {// contrato para manejar portadas de libros

    // Sube la portada de un libro usando su código identificador y el archivo recibido
    String uploadCoverPage(String bookIdentifierCode, MultipartFile multipartFile) throws IOException;

    // Obtiene la portada (como bytes) usando el título del libro y el nombre del archivo
    byte[] getCoverPage(String title, String fileName) throws IOException;

    // Devuelve el tipo MIME (formato) de una imagen a partir de su nombre de archivo
    String mimeType(String filename) throws IOException;
}
