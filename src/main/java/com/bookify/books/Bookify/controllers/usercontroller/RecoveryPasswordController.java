package com.bookify.books.Bookify.controllers.usercontroller;

import com.bookify.books.Bookify.controllers.paths.ApiPaths;
import com.bookify.books.Bookify.model.dto.userdto.RecoveryPasswordDTO;
import com.bookify.books.Bookify.shared.interfaces.userinterface.RecoveryPasswordInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.API_URL + ApiPaths.BOOKIFY_API_ID)
public class RecoveryPasswordController {

    private final RecoveryPasswordInterface recoveryPasswordInterface;

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody RecoveryPasswordDTO recoveryPasswordDTO) {
        String updatedPassword = recoveryPasswordInterface.UpdatePassword(recoveryPasswordDTO);
        return new ResponseEntity<>(updatedPassword, HttpStatus.OK);
    }
}
