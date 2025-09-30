package com.bookify.books.Bookify.shared.interfaces.checkoutinterfaces;

import com.bookify.books.Bookify.model.dto.paymentdto.OrderDTO;
import com.bookify.books.Bookify.model.entities.paymententities.OrderEntity;

import java.util.List;

public interface OrderService {// contrato para manejar órdenes de compra
    // Crea una nueva orden a partir de un libro (isbn) y una cantidad
    OrderDTO createOrder (String isbn, int quantity);
    // Busca una orden específica usando su número de orden
    OrderEntity getOrders(String orderNumber);
    // Devuelve todas las órdenes registradas en el sistema
    List<OrderEntity> getAllOrders();
}
