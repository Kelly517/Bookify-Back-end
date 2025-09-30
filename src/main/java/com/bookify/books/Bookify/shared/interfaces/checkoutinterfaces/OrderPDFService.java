package com.bookify.books.Bookify.shared.interfaces.checkoutinterfaces;

import com.bookify.books.Bookify.model.dto.bookdto.DtoLibro;
import com.bookify.books.Bookify.model.dto.paymentdto.OrderDTO;

import java.io.IOException;

public interface OrderPDFService {// contrato para generar un PDF de una orden de compra
    // Crea un PDF a partir de un OrderDTO y devuelve el archivo en bytes
    // Puede lanzar IOException si ocurre un error al generar o manejar el archivo
    byte[] createOrderPdf(OrderDTO orderDTO) throws IOException;
}
