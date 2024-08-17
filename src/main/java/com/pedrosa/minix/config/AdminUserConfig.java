package com.pedrosa.minix.config;

import com.pedrosa.minix.entities.User;
import com.pedrosa.minix.entities.enums.RoleValue;
import com.pedrosa.minix.repositories.RoleRepository;
import com.pedrosa.minix.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class AdminUserConfig implements CommandLineRunner {

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

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

        var roleAdmin = roleRepository.findByName(RoleValue.ADMIN.name());
        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin already exists");
                },
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.getRoles().add(roleAdmin.get());
                    userRepository.save(user);
                }
        );

        var roleBasic = roleRepository.findByName(RoleValue.BASIC.name());
        var userBasic = userRepository.findByUsername("mathews");

        userBasic.ifPresentOrElse(
                user -> {
                    System.out.println("this basic user already exists");
                },
                () -> {
                    var user = new User();
                    user.setUsername("mathews");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.getRoles().add(roleBasic.get());
                    userRepository.save(user);
                }
        );
    }
}
