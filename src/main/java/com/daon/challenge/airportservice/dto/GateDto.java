package com.daon.challenge.airportservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GateDto {
    private final Long id;
    private final String name;
    private final FlightDto flightDto;
}
