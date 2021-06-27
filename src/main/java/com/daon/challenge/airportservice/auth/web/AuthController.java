package com.daon.challenge.airportservice.auth.web;

import com.daon.challenge.airportservice.auth.dto.AuthRequestDto;
import com.daon.challenge.airportservice.auth.dto.AuthResponseDto;
import com.daon.challenge.airportservice.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public AuthResponseDto authenticate(@RequestBody AuthRequestDto authRequestDto) {
        return authService.authenticate(authRequestDto);
    }
}
