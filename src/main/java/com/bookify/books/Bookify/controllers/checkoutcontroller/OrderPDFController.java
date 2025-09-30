package com.bookify.books.Bookify.controllers.checkoutcontroller;

import com.bookify.books.Bookify.shared.interfaces.checkoutinterfaces.OrderPDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1")
@RequiredArgsConstructor
public class OrderPDFController {

    private final OrderPDFService orderPDFService;


}
