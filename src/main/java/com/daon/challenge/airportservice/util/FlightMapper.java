package com.daon.challenge.airportservice.util;

import com.daon.challenge.airportservice.dto.FlightDto;
import com.daon.challenge.airportservice.entity.Flight;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class FlightMapper {

    public FlightDto toDto(Flight flight) {
        return new FlightDto(flight.getId(), flight.getCode(), flight.getArrival(), flight.getNextDeparture());
    }

    public List<FlightDto> toDtos(List<Flight> flights) {
        return flights.stream()
                    .map(FlightMapper::toDto)
                    .collect(toList());
    }
}
