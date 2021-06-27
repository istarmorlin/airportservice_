package com.daon.challenge.airportservice.auth.config;

import com.daon.challenge.airportservice.auth.service.AirportserviceUserDetailsService;
import com.daon.challenge.airportservice.auth.token.TokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.expiration.time}")
    private Long expirationTime;

    @Value("${jwt.secret}")
    private String secret;

    private final AirportserviceUserDetailsService userDetailsService;

    @Bean
    public TokenProperties tokenProperties() {
        return new TokenProperties(expirationTime, secret);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);

        return authenticationProvider;
    }
}
