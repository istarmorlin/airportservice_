package com.daon.challenge.airportservice.util;

import com.daon.challenge.airportservice.dto.GateDto;
import com.daon.challenge.airportservice.entity.Flight;
import com.daon.challenge.airportservice.entity.Gate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GateMapperTest {

    @Test
    @DisplayName("Test mapping to DTO")
    public void testToDto() {
        Gate gate = prepareGate();

        GateDto result = GateMapper.toDto(gate);

        assertEquals(101L, result.getId());
        assertEquals("name", result.getName());
        assertEquals(2, result.getFlightDtos().size());
        IntStream.range(0, 2).forEach(n -> {
            assertEquals(n + 1, result.getFlightDtos().get(n).getId());
            assertEquals("code" + (n + 1), result.getFlightDtos().get(n).getCode());
        });
    }

    private Gate prepareGate() {
        var arrival = LocalDateTime.of(2021, 1, 12, 1, 0);
        var departure = LocalDateTime.of(2021, 1, 13, 14, 30);

        var opening = LocalTime.of(8, 0);
        var closing = LocalTime.of(20, 0);
        var flights = List.of(new Flight(1L, "code1", arrival, departure, null),
                            new Flight(2L, "code2", arrival.minusHours(5), departure.minusHours(5), null));
        return new Gate(101L, "name", opening, closing, flights);
    }

} 
