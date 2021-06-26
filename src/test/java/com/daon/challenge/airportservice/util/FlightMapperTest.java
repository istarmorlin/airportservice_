package com.daon.challenge.airportservice.util;

import com.daon.challenge.airportservice.dto.FlightDto;
import com.daon.challenge.airportservice.entity.Flight;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlightMapperTest {

    @Test
    @DisplayName("Test mapping to DTO")
    public void testToDto() {
        Flight flight = prepareFlight();

        FlightDto result = FlightMapper.toDto(flight);

        assertEquals(1L, result.getId());
        assertEquals("code", result.getCode());

    }

    private Flight prepareFlight() {
        var arrival = LocalDateTime.of(2021, 1, 12, 1, 0);
        var departure = LocalDateTime.of(2021, 1, 13, 14, 30);

        return new Flight(1L, "code", arrival, departure, null);
    }
} 
