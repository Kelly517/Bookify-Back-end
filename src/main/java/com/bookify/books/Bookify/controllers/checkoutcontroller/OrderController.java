package com.bookify.books.Bookify.controllers.checkoutcontroller;

import com.bookify.books.Bookify.controllers.paths.ApiPaths;
import com.bookify.books.Bookify.shared.interfaces.checkoutinterfaces.OrderService;
import com.bookify.books.Bookify.model.dto.paymentdto.OrderDTO;
import com.bookify.books.Bookify.model.entities.paymententities.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(ApiPaths.API_URL + ApiPaths.BOOKIFY_API_ID)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order-response/{bookIdentifierCode}/{quantity}")
    public ResponseEntity<OrderDTO> createOrderAndDetails(@PathVariable String bookIdentifierCode, @PathVariable int quantity) {
        OrderDTO order = orderService.createOrder(bookIdentifierCode, quantity);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/get/{orderNumber}")
    public ResponseEntity<OrderEntity> getAllOrders(@PathVariable String orderNumber) {
        OrderEntity orderGet = orderService.getOrders(orderNumber);
        return new ResponseEntity<>(orderGet, HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderEntity>> allOrders() {
        List<OrderEntity> orderEntityList = orderService.getAllOrders();
        return new ResponseEntity<>(orderEntityList, HttpStatus.OK);
    }
}
