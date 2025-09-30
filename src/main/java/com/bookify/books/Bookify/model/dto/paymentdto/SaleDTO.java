package com.bookify.books.Bookify.model.dto.paymentdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDTO {

    private String paymentStatus;// Estado del pago (ej: PAGADO, PENDIENTE, FALLIDO)
    private String userEmail;
    private int amount;
    private String bookIdentifierCode;
    private String paymentId;
}
