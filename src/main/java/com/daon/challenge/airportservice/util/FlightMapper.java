package com.daon.challenge.airportservice.util;

import com.daon.challenge.airportservice.dto.FlightDto;
import com.daon.challenge.airportservice.entity.Flight;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FlightMapper {

    public FlightDto toDto(Flight flight) {
        return new FlightDto(flight.getId(), flight.getCode(), flight.getArrival(), flight.getNextDeparture());
    }
}
