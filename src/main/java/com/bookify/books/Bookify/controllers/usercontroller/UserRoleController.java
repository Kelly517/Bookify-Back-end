package com.bookify.books.Bookify.controllers.usercontroller;

import com.bookify.books.Bookify.controllers.paths.ApiPaths;
import com.bookify.books.Bookify.controllers.paths.UserRolePaths;
import com.bookify.books.Bookify.model.dto.userdto.UserRoleDTO;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.service.userservices.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.API_URL + ApiPaths.BOOKIFY_API_ID)
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping(UserRolePaths.POST_CREATE_ROLE)
    public ResponseEntity<UserRoleDTO> createNewRole(@Valid @RequestBody UserRoleDTO userRoleDTO) {
        UserRoleDTO newRole = userRoleService.createNewRole(userRoleDTO);
        return new ResponseEntity<>(newRole, HttpStatus.CREATED);
    }

    @PutMapping(UserRolePaths.PUT_UPDATE_ROLE)
    public ResponseEntity<User> updateUserRole(@Valid @PathVariable String email, @PathVariable String roleName) {
        User updateRole = userRoleService.assignUserRole(email, roleName);
        return new ResponseEntity<>(updateRole, HttpStatus.OK);
    }
}
