package com.upskilling.experiment.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.enums.UserRole;
import com.upskilling.experiment.repository.UserRepository;


@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(new User("admin", passwordEncoder.encode("password"), UserRole.ADMIN));
            System.out.println("--- ADMIN User Created (admin:password) ---");
        }
        
        if (userRepository.findByUsername("manager").isEmpty()) {
            userRepository.save(new User("manager", passwordEncoder.encode("password"), UserRole.MANAGER));
            System.out.println("--- MANAGER User Created (manager:password) ---");
        }
        
        if (userRepository.findByUsername("member1").isEmpty()) {
            userRepository.save(new User("member1", passwordEncoder.encode("password"), UserRole.MEMBER));
            System.out.println("--- MEMBER User Created (member1:password) ---");
        }
    }
}