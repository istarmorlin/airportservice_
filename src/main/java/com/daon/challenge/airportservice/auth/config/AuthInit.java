package com.daon.challenge.airportservice.auth.config;

import com.daon.challenge.airportservice.auth.entity.User;
import com.daon.challenge.airportservice.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@Configuration
public class AuthInit {

    @Bean
    @Profile("dev")
    public CommandLineRunner authInitRunner(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.deleteAll();

            Stream.of(new User(null, "user", passwordEncoder.encode("user"), "USER"),
                    new User(null, "admin", passwordEncoder.encode("admin"), "ADMIN"))
                    .forEach(userRepository::save);
        };
    }
}
