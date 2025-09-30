package com.bookify.books.Bookify.shared.interfaces.coverpageinterface;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
// contrato para manejar fotos de perfil de usuario
public interface UploadProfilePhotoInterface {

    // Sube la foto de perfil de un usuario usando su id y el archivo recibido
    String uploadCoverPage(Long userId, MultipartFile multipartFile) throws IOException;

    // Obtiene la foto de perfil (como bytes) usando el id del usuario y el nombre del archivo
    byte[] getProfilePhoto(Long userId, String fileName) throws IOException;

    // Devuelve el tipo MIME (formato) de la imagen a partir de su nombre de archivo
    String mimeType(String filename) throws IOException;
}
