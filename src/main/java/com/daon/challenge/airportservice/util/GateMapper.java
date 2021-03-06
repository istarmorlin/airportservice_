package com.daon.challenge.airportservice.util;

import com.daon.challenge.airportservice.dto.GateDto;
import com.daon.challenge.airportservice.entity.Gate;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class GateMapper {

    public GateDto toDto(Gate gate) {
        var flightDtos = Optional.ofNullable(gate.getFlights())
                                        .map(FlightMapper::toDtos)
                                        .orElse(null);

        return new GateDto(gate.getId(), gate.getName(), gate.getOpeningTime(), gate.getClosingTime(), flightDtos);
    }
}
