package com.bookify.books.Bookify.repository.checkoutrepositories;

import com.bookify.books.Bookify.model.entities.paymententities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderNumber(String orderNumber);
}
