package com.bookify.books.Bookify.email.service;

import com.bookify.books.Bookify.exceptions.emailexceptions.EmailSenderException;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    @Value("${spring.mail.username}")
    private String fromAddress;

    private final Dotenv dotenv = Dotenv.load();
    private final static Logger LOG = LoggerFactory.getLogger(EmailSenderService.class);
    private final JavaMailSender javaMailSender;

    @Async
    public void setJavaMailSender(String to, String validatorCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(dotenv.get("SET_SUBJECT"));
            helper.setText(dotenv.get("SET_MESSAGE"), validatorCode);
            helper.setFrom(fromAddress);

            javaMailSender.send(mimeMessage);
        } catch (EmailSenderException emailSenderException) {
            throw new EmailSenderException("An error occurred with the email or the service");
        } catch (Exception exception) {
            LOG.error("An error occurred with the email: {}", exception.getMessage());
        }
    }

    @Async
    public void setJavaMailUpdateSender(String to, String updateLink) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject("Link de recuperación de contraseña");
            helper.setText(
                    "Este es su link para restaurar su contraseña: " + updateLink,
                    "<p>Este es su link para restaurar su contraseña: " +
                            "<a href=\"" + updateLink + "\">Restaurar contraseña</a></p>"
            );
            helper.setFrom(fromAddress);

            javaMailSender.send(mimeMessage);
        } catch (EmailSenderException emailSenderException) {
            throw new EmailSenderException("An error occurred with the email or the service");
        } catch (Exception exception) {
            LOG.error("An error occurred with the email: {}", exception.getMessage());
        }
    }

    @Async
    public void setJavaMailPdfBuySender(String to, byte[] pdfBytes, String fileName, String orderNumber, String name, String total) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject("RESUMEN COMPRA 🐥📚");

            String template = new String(
                    Objects.requireNonNull(getClass()
                            .getClassLoader()
                            .getResourceAsStream("html/email.html"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );

            String html = template
                    .replace("${ORDER_NUMBER}", emailNonNull(orderNumber))
                    .replace("${CUSTOMER_NAME}", emailNonNull(name))
                    .replace("${TOTAL}", emailNonNull(total));

            helper.setText(html, true);
            helper.setFrom(fromAddress);

            ByteArrayResource pdfResource = new ByteArrayResource(pdfBytes);
            helper.addAttachment(fileName, pdfResource);

            javaMailSender.send(mimeMessage);
        } catch (EmailSenderException emailSenderException) {
            throw new EmailSenderException("An error occurred with the email or the service");
        } catch (Exception exception) {
            LOG.error("An error occurred with the email: {} - {}", exception.getMessage(), to);
        }
    }

    private String emailNonNull(String emailNonNull) {
        return emailNonNull == null ? "" : emailNonNull;
    }
}
