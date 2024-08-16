package com.pedrosa.minix.config;

import com.pedrosa.minix.entities.Role;
import com.pedrosa.minix.entities.User;
import com.pedrosa.minix.repositories.RoleRepository;
import com.pedrosa.minix.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin already exists");
                },
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );

        var roleBasic = roleRepository.findByName(Role.Values.BASIC.name());
        var userBasic = userRepository.findByUsername("mathews");

        userBasic.ifPresentOrElse(
                user -> {
                    System.out.println("this basic user already exists");
                },
                () -> {
                    var user = new User();
                    user.setUsername("mathews");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleBasic));
                    userRepository.save(user);
                }
        );

    }
}
