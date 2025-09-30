package com.bookify.books.Bookify.model.mappers.checkout;

import com.bookify.books.Bookify.model.dto.paymentdto.OrderDetailDTO;
import com.bookify.books.Bookify.model.entities.paymententities.OrderDetailEntity;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class OrderDetailMapper {

    /*
     * Convierte un OrderDetailEntity en una entidad OrderDetailDTO (para guardar en BD)
     * Este mapper es especial, porque no se llama directamente en el servicio
     * Se crea el mapper, pero se utiliza en el mapper principal de orderDTOS, porque este mapper
     * es para los detalles de la orden, o sea que es un proceso interno
     */
    public static OrderDetailDTO OrderDetailsEntityToDto(OrderDetailEntity orderDetail) {
        return OrderDetailDTO.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .quantity(orderDetail.getQuantity())
                .status(orderDetail.getStatus())
                .totalAmount(orderDetail.getTotalAmount())
                .orderDate(orderDetail.getOrderDate())
                .bookEntity(orderDetail.getBookEntity())
                .build();
    }
}
