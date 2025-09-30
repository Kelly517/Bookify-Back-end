package com.bookify.books.Bookify.service.coverpageservices;

import com.bookify.books.Bookify.model.entities.bookentities.UploadCoverPageMessage;
import com.bookify.books.Bookify.repository.bookrepositories.BookRepository;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class UploadCoverPageConsumer {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Value("${bookify.upload-dir}")
    private String uploadDir;

    private final static Logger LOG = LoggerFactory.getLogger(UploadCoverPageConsumer.class);

    @RabbitListener(queues = "cover-image-queue")
    public void processImageUpload(UploadCoverPageMessage message) {
        try {
            Path path = Paths.get(uploadDir);
            Files.createDirectories(path);

            Path filePath = path.resolve(message.getFileName());
            Files.write(filePath, message.getFileContent());

            switch (message.getType()) {
                case "BOOK" -> bookRepository.findByBookIdentifierCodeAndIsActiveTrue(message.getReferenceId()).ifPresent(book -> {
                    book.setFilePathCoverPage(message.getFileName());
                    bookRepository.save(book);
                });

                case "USER" -> {
                    Long userId = Long.parseLong(message.getReferenceId());
                    userRepository.findById(userId).ifPresent(user -> {
                        user.setProfilePhoto(message.getFileName());
                        userRepository.save(user);
                    });
                }

                default -> LOG.warn("Non valid image type: {}", message.getType());
            }

            LOG.info("Image processed and saved: {}", filePath);

        } catch (IOException e) {
            LOG.error("Error saving the image: {}", e.getMessage());
        } catch (Exception e) {
            LOG.error("Internal server error: {}", e.getMessage());
        }
    }
}
