    package com.bookify.books.Bookify.model.entities.paymententities;

    import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Table(name = "order_detail")
    public class OrderDetailEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)// Se autogenera en la BD
        @Column(name = "order_detail_id")
        private Long orderDetailId;// ID único para cada detalle de orden

        // ----------------------------
        // Relación con el libro comprado
        // ----------------------------
        @ManyToOne(fetch = FetchType.EAGER)// Muchos detalles de orden pueden estar asociados al mismo libro
        @JoinColumn(name = "product_id")// En la tabla order_detail, "product_id" apunta a la tabla book
        @JsonManagedReference(value = "orderDetail-book")
        // Se usa para controlar la serialización JSON (este es el lado "padre" que se exporta)
        private BookEntity bookEntity;


        // ----------------------------
        // Relación con la orden principal
        // ----------------------------
        @ManyToOne
        @JoinColumn(name = "order_id")    // En la tabla order_detail, "order_id" referencia a la tabla order
        @JsonBackReference(value = "order-orderDetail")
        // Controla la serialización JSON (este es el lado "hijo", no se exporta de nuevo para evitar ciclos)
        private OrderEntity order;

        // ----------------------------
        // Información del pago
        // ----------------------------
        @Column(name = "payment_date", nullable = false)
        private LocalDate orderDate;// Fecha en la que se realizó el pago

        @Column(name = "quantity")
        private int quantity;// Cantidad de productos/libros en la orden

        @Column(name = "total_amount")
        private Double totalAmount;// Valor total de este detalle

        @Column(name = "status", nullable = false)
        private String status;// Estado del detalle (ej: "PAGADO", "PENDIENTE", "CANCELADO")
    }
