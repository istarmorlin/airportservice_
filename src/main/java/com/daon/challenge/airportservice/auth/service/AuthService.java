package com.daon.challenge.airportservice.auth.service;

import com.daon.challenge.airportservice.auth.dto.AuthRequestDto;
import com.daon.challenge.airportservice.auth.dto.AuthResponseDto;
import com.daon.challenge.airportservice.auth.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public AuthResponseDto authenticate(AuthRequestDto authRequestDto) {
        var username = authRequestDto.getUsername();
        var password = authRequestDto.getPassword();

        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return new AuthResponseDto(null, "Missing credentials!");
        }

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var token = tokenProvider.generateToken(authentication);
        return new AuthResponseDto("Bearer " + token, "Successfully generated token!");
    }
}
