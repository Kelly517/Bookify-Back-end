package com.bookify.books.Bookify.controllers.coverpages;

import com.bookify.books.Bookify.controllers.paths.ApiPaths;
import com.bookify.books.Bookify.controllers.paths.bookpaths.UploadCoverPagePath;
import com.bookify.books.Bookify.shared.interfaces.coverpageinterface.UploadCoverPageInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(ApiPaths.API_URL + ApiPaths.BOOKIFY_API_ID)
@RequiredArgsConstructor
public class UploadCoverPageController {

    private final UploadCoverPageInterface uploadCoverPageInterface;
    private final static Logger LOG = LoggerFactory.getLogger(UploadCoverPageController.class);

    @PostMapping(UploadCoverPagePath.POST_URL)
    public ResponseEntity<String> uploadCoverPage(@PathVariable("bookIdentifierCode") String bookIdentifierCode, @RequestPart("file")MultipartFile file) {
        try {
            String coverPageUrl = uploadCoverPageInterface.uploadCoverPage(bookIdentifierCode, file);
            LOG.info("This is the cover page file: {}", coverPageUrl);
            return new ResponseEntity<>("The cover page has ben uploaded " + coverPageUrl, HttpStatus.CREATED);
        } catch (IOException ioException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ioException.getMessage());
        }
    }

    @GetMapping(UploadCoverPagePath.GET_URL)
    public ResponseEntity<byte[]> viewImage(@PathVariable String title, @PathVariable String file) {
        try {
            byte[] image = uploadCoverPageInterface.getCoverPage(title, file);
            String mimeType = uploadCoverPageInterface.mimeType(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(image);
        } catch (IOException ioException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
