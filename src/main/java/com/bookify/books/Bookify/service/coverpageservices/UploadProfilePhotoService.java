package com.bookify.books.Bookify.service.coverpageservices;

import com.bookify.books.Bookify.configuration.RabbitMQConfig;
import com.bookify.books.Bookify.exceptions.bookexceptions.BookAlreadyexistsException;
import com.bookify.books.Bookify.model.entities.bookentities.UploadCoverPageMessage;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import com.bookify.books.Bookify.shared.interfaces.coverpageinterface.UploadProfilePhotoInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadProfilePhotoService implements UploadProfilePhotoInterface {

    @Value("${bookify.upload-dir}")
    private String uploadDir;
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;
    private final static Logger LOG = LoggerFactory.getLogger(UploadProfilePhotoService.class);

    @Override
    public String uploadCoverPage(Long userId, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new RuntimeException("The file is empty");
        }

        String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        byte[] fileBytes = multipartFile.getBytes();

        Path outputPath = Paths.get(uploadDir, fileName);
        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, fileBytes);

        UploadCoverPageMessage message = new UploadCoverPageMessage("USER", String.valueOf(userId), fileName, fileBytes);
        rabbitTemplate.convertAndSend(RabbitMQConfig.COVER_IMAGE_QUEUE, message);
        LOG.info("Processing profile for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfilePhoto(fileName);
        userRepository.save(user);

        return "Processing cover image for user with id: " + userId;
    }

    private User userExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new BookAlreadyexistsException("User not found"));
    }

    @Override
    public byte[] getProfilePhoto(Long userId, String fileName) throws IOException {
        try {
            User user = userExists(userId);
            String photo = user.getProfilePhoto();
            String profilePhotoImage = Paths.get(uploadDir, photo).toString();

            Path profilePhoto = Paths.get(profilePhotoImage);
            int attempts = 0;
            while(!Files.exists(profilePhoto) && attempts < 10) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    throw new IOException("Interrupted while waiting for file: ", interruptedException);
                }
                attempts++;
            }

            if (!Files.exists(profilePhoto)) {
                throw new IOException("Cover image not found after waiting.");
            }

            return Files.readAllBytes(profilePhoto);
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
