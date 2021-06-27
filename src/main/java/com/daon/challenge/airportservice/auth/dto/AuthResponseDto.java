package com.daon.challenge.airportservice.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthResponseDto {
    private final String token;
    private final String message;
}
