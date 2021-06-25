package com.daon.challenge.airportservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class FlightDto {
    private final Long id;
    private final String code;
    private final LocalDateTime arrival;
    private final LocalDateTime nextDeparture;
}
