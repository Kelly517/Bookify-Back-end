package com.bookify.books.Bookify.service.checkoutservice;

import com.bookify.books.Bookify.model.dto.paymentdto.OrderDTO;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.List;
import com.bookify.books.Bookify.shared.interfaces.checkoutinterfaces.OrderPDFService;
import com.lowagie.text.Font;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OrderPDFServiceImpl implements OrderPDFService {
    private final static String summaryTitle = "Resumen de compra";
    StringBuilder completeName = new StringBuilder();

    @Override
    public byte[] createOrderPdf(OrderDTO orderDTO) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            Font title = FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Color.BLACK);
            Paragraph h1 = new Paragraph(summaryTitle, title);
            h1.setAlignment(Element.ALIGN_CENTER);
            h1.setSpacingAfter(20f);
            document.add(h1);

            createContent(document, orderDTO);
            return byteArrayOutputStream.toByteArray();
        }
    }

    private void createContent(Document document, OrderDTO orderDTO) {
        document.add(new Paragraph("..........................................................................."));
        document.add(new Paragraph("N° Orden: " + orderDTO.getOrderNumber()));
        document.add(new Paragraph("Fecha : " + orderDTO.getOrderDetailEntities().getOrderDate()));
        document.add(new Paragraph("..........................................................................."));

        Font subtitle = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15);
        document.add(new Paragraph("Detalles del comprador", subtitle));
        document.add(new Paragraph(createName(orderDTO).toString()));
        document.add(new Paragraph("Email: " + orderDTO.getUser().getEmail()));
        document.add(new Paragraph("Rol: " + orderDTO.getUser().getUserRole().getRoleName()));

        document.add(new Paragraph("..........................................................................."));
        createPurchasedBooks(document, orderDTO, subtitle);

        document.add(new Paragraph("..........................................................................."));
        document.add(new Paragraph("Estado de la orden: " + orderDTO.getOrderDetailEntities().getStatus()));
        document.add(new Paragraph("TOTAL: " + orderDTO.getTotalAmount()));
        document.close();
    }

    private void createPurchasedBooks(Document document, OrderDTO orderDTO, Font subtitle) {
        document.add(new Paragraph("Libros comprados: ", subtitle));

        List list = new List(List.ORDERED);
        list.setSymbolIndent(12f);
        list.setIndentationLeft(20f);

        for (BookEntity book : orderDTO.getUser().getPurchasedBooks()) {
            String line = book.getTitle()
                    + " - "
                    + book.getAuthor().getUserName() + "\n"
                    + "Total: " + book.getPrice() + " "
                    + " * "
                    + orderDTO.getOrderDetailEntities().getQuantity();

            list.add(new ListItem(line));
        }

        document.add(list);
        document.add(Chunk.NEWLINE);
    }

    private StringBuilder createName(OrderDTO orderDTO) {
        return completeName
                .append("Nombre comprador: ")
                .append(orderDTO
                        .getUser()
                        .getName())
                .append(" ")
                .append(orderDTO
                        .getUser()
                        .getLastname());
    }
}
