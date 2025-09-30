package com.bookify.books.Bookify.controllers.usercontroller;

import com.bookify.books.Bookify.controllers.paths.ApiPaths;
import com.bookify.books.Bookify.controllers.paths.LoginPaths;
import com.bookify.books.Bookify.service.userservices.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.API_URL + ApiPaths.BOOKIFY_API_ID)
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping(LoginPaths.LOGIN_USER)
    public ResponseEntity<String> loginUserWithEmailAndPassword(@PathVariable String email, @PathVariable String password) {
        String response = loginService.login(email, password);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
