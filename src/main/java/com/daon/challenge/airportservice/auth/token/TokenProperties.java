package com.daon.challenge.airportservice.auth.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenProperties {
    private final Long expirationTime;
    private final String secret;
}
