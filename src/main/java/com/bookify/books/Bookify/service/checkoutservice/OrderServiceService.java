package com.bookify.books.Bookify.service.checkoutservice;

import com.bookify.books.Bookify.email.service.EmailSenderService;
import com.bookify.books.Bookify.exceptions.userexceptions.UserExistsException;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import com.bookify.books.Bookify.security.JwtUtil;
import com.bookify.books.Bookify.shared.interfaces.checkoutinterfaces.OrderService;
import com.bookify.books.Bookify.model.dto.paymentdto.OrderDTO;
import com.bookify.books.Bookify.model.entities.paymententities.OrderDetailEntity;
import com.bookify.books.Bookify.model.entities.paymententities.OrderEntity;
import com.bookify.books.Bookify.model.mappers.checkout.OrderDTOsMapper;
import com.bookify.books.Bookify.repository.checkoutrepositories.OrderRepository;
import com.bookify.books.Bookify.repository.bookrepositories.BookRepository;
import com.bookify.books.Bookify.shared.interfaces.securityinterfaces.GenerateUniqueCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceService implements OrderService, GenerateUniqueCode {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final OrderDTOsMapper orderDTOsMapper;
    private final OrderPDFServiceImpl orderPDFService;
    private final EmailSenderService emailSenderService;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;
    private final UserRepository userRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderServiceService.class);

    @Override
    public OrderDTO createOrder(String bookIdentifierCode, int quantity) {
        Optional<BookEntity> bookEntity = bookRepository.findByBookIdentifierCodeAndIsActiveTrue(bookIdentifierCode);

        if (bookEntity.isPresent()) {
            String orderNumber = generateUniqueId();
            OrderEntity order = new OrderEntity();
            order.setOrderNumber(orderNumber);
            order.setUser(findUserToOrder());

            OrderDetailEntity orderDetail = new OrderDetailEntity();
            BookEntity book = bookEntity.get();

            addDetailsToOrder(book, orderDetail, quantity, order);
            order.setOrderDetails(orderDetail);
            addToListPurchasedBooks(book, order);

            OrderEntity savedOrder = orderRepository.save(order);

            OrderDTO orderDTO = orderDTOsMapper.convertEntityToDto(savedOrder);
            try {
                byte[] pdfBytes = orderPDFService.createOrderPdf(orderDTO);
                emailSenderService.setJavaMailPdfBuySender(
                        orderDTO.getUser().getEmail(),
                        pdfBytes,
                        "resumen-compra.pdf",
                        orderNumber,
                        orderDTO.getUser().getName(),
                        Double.toString(orderDTO.getTotalAmount()));
                LOGGER.info("Email sent to: {}", orderDTO.getUser().getEmail());
            } catch (IOException ioException) {
                LOGGER.error("Error sending PDF");
                throw new RuntimeException("Error enviando pdf ", ioException);
            }
            return orderDTO;
        }
        LOGGER.error("An error occurred with the order");
        throw new RuntimeException("Ocurrió un error con la orden");
    }

    private void addDetailsToOrder(BookEntity book, OrderDetailEntity orderDetail, int quantity, OrderEntity order) {
        orderDetail.setBookEntity(book);
        orderDetail.setQuantity(quantity);
        orderDetail.setOrderDate(LocalDate.now());
        orderDetail.setTotalAmount(book.getPrice() * quantity);
        orderDetail.setOrder(order);
        orderDetail.setStatus("COMPLETED");
    }

    private void addToListPurchasedBooks(BookEntity book, OrderEntity order) {
        User user = order.getUser();

        List<BookEntity> purchased = user.getPurchasedBooks();
        if (purchased == null) {
            purchased = new ArrayList<>();
            user.setPurchasedBooks(purchased);
        }

        boolean alreadyAdded = purchased.stream()
                .anyMatch(b -> b.getBookId().equals(book.getBookId()));
        if (!alreadyAdded) {
            purchased.add(book);
        }
    }

    private User findUserToOrder() {
        String token = request.getHeader("Authorization").replace("Bearer", "");
        String email = jwtUtil.extractEmail(token);

        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserExistsException("User not found"));
    }

    @Override
    public OrderEntity getOrders(String orderNumber) {
        Optional<OrderEntity> orderEntity = orderRepository.findByOrderNumber(orderNumber);

        if (orderEntity.isEmpty()) {
            throw new RuntimeException("Error");
        }

        return orderEntity.get();
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }
}
