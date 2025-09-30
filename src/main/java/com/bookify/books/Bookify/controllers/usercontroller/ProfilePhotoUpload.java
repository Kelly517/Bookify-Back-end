package com.bookify.books.Bookify.controllers.usercontroller;

import com.bookify.books.Bookify.controllers.paths.ApiPaths;
import com.bookify.books.Bookify.shared.interfaces.coverpageinterface.UploadProfilePhotoInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(ApiPaths.API_URL + ApiPaths.BOOKIFY_API_ID)
@RequiredArgsConstructor
public class ProfilePhotoUpload {
    private final UploadProfilePhotoInterface uploadProfilePhotoInterface;
    private final Logger LOG = LoggerFactory.getLogger(ProfilePhotoUpload.class);

    @PostMapping("/profile/photo/{userId}")
    public ResponseEntity<String> bookylistCoverPage(@PathVariable Long userId, @RequestPart("file") MultipartFile file) {
        try {
            String coverPageUrl = uploadProfilePhotoInterface.uploadCoverPage(userId, file);
            return new ResponseEntity<>("Profile photo has ben uploaded " + coverPageUrl, HttpStatus.CREATED);
        } catch (IOException ioException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ioException.getMessage());
        }
    }

    @GetMapping("/profile/photo/{userId}/{file}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long userId, @PathVariable String file) {
        try {
            byte[] image = uploadProfilePhotoInterface.getProfilePhoto(userId, file);
            String mimeType = uploadProfilePhotoInterface.mimeType(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(image);
        } catch (IOException ioException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
