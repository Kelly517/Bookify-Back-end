package com.bookify.books.Bookify.controllers.usercontroller;

import com.bookify.books.Bookify.controllers.paths.ApiPaths;
import com.bookify.books.Bookify.controllers.paths.UserPaths;
import com.bookify.books.Bookify.model.dto.userdto.CodeVerificationDto;
import com.bookify.books.Bookify.model.dto.userdto.UpdateUserDto;
import com.bookify.books.Bookify.model.dto.userdto.UserDTO;
import com.bookify.books.Bookify.service.userservices.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(ApiPaths.API_URL + ApiPaths.BOOKIFY_API_ID)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final static Logger LOG = LoggerFactory.getLogger(UserController.class.getName());

    @PostMapping(UserPaths.POST_REGISTER)
    public ResponseEntity<String> userRegisterFromBookifyLibrary(@Valid @RequestBody UserDTO userDTO) {
        String registeredUser = userService.userRegister(userDTO);
        LOG.debug("User created: {}", registeredUser);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/code-verify")
    public ResponseEntity<Map<String, String>> verifyCodeAndRegister(@RequestBody CodeVerificationDto codeVerificationDto) {
        UserDTO userDTO = userService.verifyCodeAndSave(codeVerificationDto.getEmail(), codeVerificationDto.getCode());

        if (userDTO != null) {
            return ResponseEntity
                    .ok(Map.of
                            ("email", codeVerificationDto.getEmail(),
                                    "code", codeVerificationDto.getCode()));
        }
        return ResponseEntity
                .badRequest()
                .body(Map.of
                        ("Email",
                                "not found"));
    }

    @PutMapping("/user/email/update/{email}")
    public ResponseEntity<String> sendUpdatePassword(@PathVariable String email) {
        return new ResponseEntity<>(userService.updatePasswordEmailSender(email), HttpStatus.OK);
    }

    @GetMapping(UserPaths.GET_USER)
    public ResponseEntity<UserDTO> userGetWithEmail(@PathVariable String email) {
        UserDTO searchedUser = userService.getUser(email);
        return new ResponseEntity<>(searchedUser, HttpStatus.OK);
    }

    @GetMapping("get-all/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        Page<UserDTO> allUsers = userService.getAllUsers(pageable);
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PutMapping(UserPaths.PUT_USER)
    public ResponseEntity<UserDTO> userPutWithEmail(@PathVariable String email, @Valid @RequestBody UpdateUserDto userDTO) {
        UserDTO updatedUser = userService.userUpdate(email, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping(UserPaths.DELETE_USER)
    public ResponseEntity<String> deleteUserByEmail(@PathVariable String email) {
        String deletedUser = userService.userDelete(email);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }

}
