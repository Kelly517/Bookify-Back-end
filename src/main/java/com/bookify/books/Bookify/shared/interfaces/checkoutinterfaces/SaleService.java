package com.bookify.books.Bookify.shared.interfaces.checkoutinterfaces;

import com.bookify.books.Bookify.model.dto.paymentdto.SaleDTO;

public interface SaleService {// contrato para manejar pagos realizados
    // Procesa un pago exitoso recibido desde la pasarela
    // Recibe un SaleDTO con los datos del pago y devuelve un SaleDTO confirmado
    SaleDTO processSuccessfullPayment(SaleDTO saleDTO);
}
