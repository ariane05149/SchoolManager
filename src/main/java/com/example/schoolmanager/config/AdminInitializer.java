package com.example.schoolmanager.config;

import com.example.schoolmanager.user.User;
import com.example.schoolmanager.user.UserRepository;
import com.example.schoolmanager.user.Role;
import com.example.schoolmanager.user.AuthProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository repo, PasswordEncoder encoder) {
        return args -> {

            if (!repo.existsByEmail("admin@school.com")) {

                User admin = User.builder()
                        .email("admin@school.com")
                        .name("Admin")
                        .password(encoder.encode("ad@123")) // BCrypt encoded
                        .role(Role.ADMIN)
                        .provider(AuthProvider.LOCAL)
                        .build();

                repo.save(admin);

                System.out.println("ADMIN CREATED: admin@school.com / ad@123");
            }
        };
    }
}