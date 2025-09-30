package com.bookify.books.Bookify.service.coverpageservices;

import com.bookify.books.Bookify.configuration.RabbitMQConfig;
import com.bookify.books.Bookify.exceptions.bookexceptions.BookAlreadyexistsException;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.bookify.books.Bookify.shared.interfaces.coverpageinterface.UploadCoverPageInterface;
import com.bookify.books.Bookify.model.entities.bookentities.UploadCoverPageMessage;
import com.bookify.books.Bookify.repository.bookrepositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadCoverPageServiceImpl implements UploadCoverPageInterface {

    @Value("${bookify.upload-dir}")
    private String uploadDir;
    private final RabbitTemplate rabbitTemplate;
    private final BookRepository bookRepository;
    private final static Logger LOG = LoggerFactory.getLogger(UploadCoverPageServiceImpl.class);

    @Override
    public String uploadCoverPage(String bookIdentifierCode, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new RuntimeException("The file is empty");
        }

        String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        byte[] fileBytes = multipartFile.getBytes();

        Path outputPath = Paths.get(uploadDir, fileName);
        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, fileBytes);

        UploadCoverPageMessage message = new UploadCoverPageMessage("BOOK", bookIdentifierCode, fileName, fileBytes);
        rabbitTemplate.convertAndSend(RabbitMQConfig.COVER_IMAGE_QUEUE, message);
        LOG.info("Processing cover image for book with identifier code: {}", bookIdentifierCode);

        BookEntity book = bookRepository.findByBookIdentifierCodeAndIsActiveTrue(bookIdentifierCode)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setFilePathCoverPage(fileName);
        bookRepository.save(book);

        return "Processing cover image for book with identifier code: " + bookIdentifierCode;
    }

    private BookEntity titleExists(String title) {
        return bookRepository.findByTitleAndIsActiveTrue(title)
                .orElseThrow(() ->
                        new BookAlreadyexistsException("Requested book already exist." + title));
    }

    @Override
    public byte[] getCoverPage(String title, String filePath) throws IOException {
        try {
            BookEntity book = titleExists(title);
            String coverFileImage = book.getFilePathCoverPage();
            String coverPathImage = Paths.get(uploadDir, coverFileImage).toString();

            Path coverPagePath = Paths.get(coverPathImage);
            int attempts = 0;
            while(!Files.exists(coverPagePath) && attempts < 10) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    throw new IOException("Interrupted while waiting for file: ", interruptedException);
                }
                attempts++;
            }

            if (!Files.exists(coverPagePath)) {
                throw new IOException("Cover image not found after waiting.");
            }

            return Files.readAllBytes(coverPagePath);
        } catch (IOException ioException) {
            throw new IOException("Image doesn't exists", ioException.getCause());
        }
    }

    @Override
    public String mimeType(String filename) throws IOException {
        Path path = Paths.get(uploadDir, filename);
        return Files.probeContentType(path);
    }
}
