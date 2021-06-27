package com.daon.challenge.airportservice.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthRequestDto {
    private final String username;
    private final String password;
}
