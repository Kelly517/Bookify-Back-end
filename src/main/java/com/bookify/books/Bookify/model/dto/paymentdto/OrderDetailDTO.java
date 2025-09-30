package com.bookify.books.Bookify.model.dto.paymentdto;

import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.bookify.books.Bookify.model.entities.paymententities.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailDTO {

    private Long orderDetailId;// Identificador único del detalle de orden
    private BookEntity bookEntity; // Libro comprado en esta orden
    private OrderEntity order;// Orden a la que pertenece este detalle
    private LocalDate orderDate;// Fecha en la que se realizó el pago
    private int quantity;// Cantidad de ejemplares comprados
    private Double totalAmount;// Monto total de este detalle
    private String status;// Estado del detalle

}
//OrderDetailDTO sirve para transportar al frontend la información de cada detalle de orden, incluyendo
// el libro comprado, la cantidad, el precio total y el estado del pago.
