package com.pedrosa.minix.services;

import com.pedrosa.minix.controllers.dto.CreateUserDto;
import com.pedrosa.minix.entities.User;
import com.pedrosa.minix.entities.enums.RoleValue;
import com.pedrosa.minix.repositories.RoleRepository;
import com.pedrosa.minix.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User newUser (User obj) {

        var basicRole = roleRepository.findByName(RoleValue.BASIC.name());
        basicRole.ifPresent(role -> obj.getRoles().add(role));

        var userFromDb = userRepository.findByUsername(obj.getUsername());
        if(userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return userRepository.save(obj);
    }

    public User fromDto(CreateUserDto userDto) {
        User user = new User();
        user.setUsername(userDto.username());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        return user;
    }

}
