package com.bookify.books.Bookify.model.entities.paymententities;

import com.bookify.books.Bookify.model.entities.userentities.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// Se genera automáticamente en la BD
    @Column(name = "order_id")
    private Long orderId;// Identificador único de la orden

    // ----------------------------
    // Relación con el usuario (cliente)
    // ----------------------------
    @ManyToOne
    @JoinColumn(name = "customer_id")
    // En la tabla "orders" se guarda el ID del usuario que hizo la compra
    private User user;

    @Column(name = "order_number")
    private String orderNumber;// Número único de la orden (ej: código de referencia)

    // ----------------------------
    // Relación con el detalle de la orden
    // ----------------------------
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_detail_id", referencedColumnName = "order_detail_id")
    // En la tabla "orders" se guarda la FK hacia "order_detail"
    private OrderDetailEntity orderDetailEntity;

    @Column(name = "total_amount")
    private Double totalAmount;// Valor total de la orden

    // Métodohelper: asegura que cuando se asigna un detalle,
    // también se actualice el total de la orden automáticamente
    public void setOrderDetails(OrderDetailEntity orderDetailEntity) {
        this.orderDetailEntity = orderDetailEntity;
        this.totalAmount = orderDetailEntity.getTotalAmount();
    }
}
