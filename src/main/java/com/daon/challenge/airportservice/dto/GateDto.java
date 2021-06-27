package com.daon.challenge.airportservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GateDto {
    private final Long id;
    private final String name;
    private final List<FlightDto> flightDtos;
}
