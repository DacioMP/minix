package com.pedrosa.minix.controllers;

import com.pedrosa.minix.controllers.dto.CreateUserDto;
import com.pedrosa.minix.entities.User;
import com.pedrosa.minix.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<User>> listUsers() {
        var users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser (@RequestBody CreateUserDto dto) {

        User obj = userService.fromDto(dto);
        userService.newUser(obj);

        return ResponseEntity.ok().build();
    }

}
