package com.pedrosa.minix.controllers;

import com.pedrosa.minix.dto.LoginRequestDto;
import com.pedrosa.minix.dto.LoginResponseDto;
import com.pedrosa.minix.repositories.UserRepository;
import com.pedrosa.minix.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login (@RequestBody LoginRequestDto loginRequestDto) {

        var jwtValue = loginService.getTokenJwt(loginRequestDto);

        var expiresIn = 300L;

        return ResponseEntity.ok(new LoginResponseDto(jwtValue, expiresIn));

    }
}
