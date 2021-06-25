package com.daon.challenge.airportservice.util;

import com.daon.challenge.airportservice.dto.GateDto;
import com.daon.challenge.airportservice.entity.Flight;
import com.daon.challenge.airportservice.entity.Gate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GateMapperTest {

    @Test
    @DisplayName("Test mapping to DTO")
    public void testToDto() {
        Gate gate = prepareGate();

        GateDto result = GateMapper.toDto(gate);

        assertEquals(101L, result.getId());
        assertEquals("name", result.getName());
        assertEquals(1L, result.getFlightDto().getId());
        assertEquals("code", result.getFlightDto().getCode());
    }

    private Gate prepareGate() {
        var arrival = LocalDateTime.of(2021, 1, 12, 1, 0);
        var departure = LocalDateTime.of(2021, 1, 13, 14, 30);

        var flight = new Flight(1L, "code", arrival, departure, null);
        return new Gate(101L, "name", flight);
    }

} 
