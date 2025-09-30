package com.bookify.books.Bookify.model.entities.bookentities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data // Lombok: genera getters, setters, toString, equals, hashCode
@AllArgsConstructor // Constructor con todos los atributos
public class UploadCoverPageMessage implements Serializable {
    // Número de versión de la clase para la serialización
    // Sirve para asegurar compatibilidad si la clase cambia con el tiempo
    private static final long serialVersionUID = 1L;


    private String type;// Tipo de mensaje (ej: "UPLOAD", "DELETE", etc.)
    private String referenceId;    // ID de referencia del libro u objeto al que pertenece la portada
    private String fileName; // Nombre del archivo de la portada (ej: "portada123.png")
    private byte[] fileContent;// Contenido binario del archivo (la imagen convertida a bytes)

}

