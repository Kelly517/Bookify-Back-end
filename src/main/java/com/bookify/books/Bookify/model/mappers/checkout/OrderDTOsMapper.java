package com.bookify.books.Bookify.model.mappers.checkout;

import com.bookify.books.Bookify.model.dto.paymentdto.OrderDTO;
import com.bookify.books.Bookify.model.entities.paymententities.OrderEntity;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class OrderDTOsMapper {

    // Convierte un OrderEntity en una entidad OrderDTO (para guardar en BD)
    public OrderDTO convertEntityToDto(OrderEntity orderEntity) {
        return OrderDTO.builder()
                .oderId(orderEntity.getOrderId())
                .orderNumber(orderEntity.getOrderNumber())
                .user(orderEntity.getUser())
                .orderDetailEntities(
                        OrderDetailMapper
                        .OrderDetailsEntityToDto(
                                orderEntity.getOrderDetailEntity())) //Aquí es donde se llama el mapper interno, de order details
                .totalAmount(orderEntity.getTotalAmount())
                .build();
    }
}
