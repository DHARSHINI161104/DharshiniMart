package com.dharshinimart.config;

import com.dharshinimart.model.Role;
import com.dharshinimart.model.User;
import com.dharshinimart.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAdmin(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByEmail("admin@dharshinimart.com")) {
                User admin = new User(
                        "admin@dharshinimart.com",
                        "Administrator",
                        new BCryptPasswordEncoder().encode("admin123"),
                        Role.ADMIN);
                userRepository.save(admin);
                System.out.println("[DharshiniMart] Admin created: admin@dharshinimart.com / admin123");
            }
        };
    }
}