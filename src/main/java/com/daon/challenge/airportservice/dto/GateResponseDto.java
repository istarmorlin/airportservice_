package com.daon.challenge.airportservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GateResponseDto {
    private final GateDto assignedGate;
    private final String responseMessage;
}
