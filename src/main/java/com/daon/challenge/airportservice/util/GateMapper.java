package com.daon.challenge.airportservice.util;

import com.daon.challenge.airportservice.dto.FlightDto;
import com.daon.challenge.airportservice.dto.GateDto;
import com.daon.challenge.airportservice.entity.Gate;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class GateMapper {

    public GateDto toDto(Gate gate) {
        FlightDto flightDto = Optional.ofNullable(gate.getFlight())
                                        .map(FlightMapper::toDto)
                                        .orElse(null);

        return new GateDto(gate.getId(), gate.getName(), flightDto);
    }
}
