package com.bookify.books.Bookify.model.dto.paymentdto;

import com.bookify.books.Bookify.model.entities.paymententities.OrderDetailEntity;
import com.bookify.books.Bookify.model.entities.userentities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDTO {

    private Long oderId; // ID único de la orden (ojo: está mal escrito, debería ser orderId)
    private String orderNumber; // Número único de la orden (ej: ORD-2025-0001)
    private User user;// Usuario que realizó la orden (lo ideal sería un UserDto con solo datos básicos)
    private OrderDetailDTO orderDetailEntities; // Detalle(s) de la orden: qué libro(s) se compraron, cantidad, total y estado
    private Double totalAmount;// Monto total de la orden
}
//OrderDTO sirve para transportar toda la información de una orden al frontend: cabecera, usuario comprador, detalles de lo comprado y el monto total.