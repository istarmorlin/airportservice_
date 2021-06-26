package com.daon.challenge.airportservice.service;

import com.daon.challenge.airportservice.dto.FlightDto;
import com.daon.challenge.airportservice.dto.GateDto;
import com.daon.challenge.airportservice.dto.GateResponseDto;
import com.daon.challenge.airportservice.entity.Flight;
import com.daon.challenge.airportservice.entity.Gate;
import com.daon.challenge.airportservice.repository.FlightRepository;
import com.daon.challenge.airportservice.repository.GateRepository;
import com.daon.challenge.airportservice.util.FlightMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GateServiceTest {

    @Mock
    private GateRepository gateRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private GateService gateService;

    @Test
    @DisplayName("Retrieve gates test")
    public void testRetrieveAllGates() {
        var gates = prepareGates();

        given(gateRepository.findAll()).willReturn(gates);

        var gateDtos = gateService.retrieveAllGates();

        assertEquals(3, gateDtos.size());
        IntStream.range(0, 3)
                .forEach(n -> {
                    assertEquals(Long.valueOf(n + 1), gateDtos.get(n).getId());
                    assertEquals("g" + (n + 1), gateDtos.get(n).getName());
                });

        verify(gateRepository, times(1)).findAll();
        verifyNoMoreInteractions(gateRepository);
    }

    @Test
    @DisplayName("Assign gate happy flow")
    public void testAssignGate() {
        var availableGates = prepareGates();
        var flight = prepareFlight();
        var flightDto = FlightMapper.toDto(flight);

        given(gateRepository.findAvailableGates()).willReturn(availableGates);
        given(flightRepository.findByCode(anyString())).willReturn(Optional.of(flight));

        GateResponseDto result = gateService.assignGate(flightDto);

        assertEquals(1L, result.getAssignedGate().getId());
        assertEquals("Successfully assigned gate", result.getResponseMessage());

        verify(gateRepository, times(1)).findAvailableGates();
        verify(flightRepository, times(1)).findByCode(anyString());
    }

    @Test
    @DisplayName("Assign gate - no available gates")
    public void testAssignGate2() {
        List<Gate> availableGates = Collections.emptyList();
        var flight = prepareFlight();
        var flightDto = FlightMapper.toDto(flight);

        given(gateRepository.findAvailableGates()).willReturn(availableGates);

        GateResponseDto result = gateService.assignGate(flightDto);

        assertNull(result.getAssignedGate());
        assertEquals("No available gate at this time", result.getResponseMessage());

        verify(gateRepository, times(1)).findAvailableGates();
        verifyNoMoreInteractions(gateRepository);
        verifyNoInteractions(flightRepository);
    }

    @Test
    @DisplayName("Assign gate - bad flight code")
    public void testAssignGate3() {
        var availableGates = prepareGates();
        var flight = prepareFlight();
        var flightDto = FlightMapper.toDto(flight);

        given(gateRepository.findAvailableGates()).willReturn(availableGates);
        given(flightRepository.findByCode(anyString())).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gateService.assignGate(flightDto));

        verify(gateRepository, times(1)).findAvailableGates();
        verify(flightRepository, times(1)).findByCode(anyString());
        verifyNoMoreInteractions(gateRepository);
        verifyNoMoreInteractions(flightRepository);
    }

    @Test
    @DisplayName("Update gate - happy flow")
    public void testUpdateGate() {
        var flightToUpdate = prepareFlight();
        var gateToUpdate = new Gate(1L, "g1", flightToUpdate);
        var gateDto = new GateDto(3L, "g3", new FlightDto(3L, "FL3", null, null));
        var flight = new Flight(9L, "FL9", null, null, null);

        given(gateRepository.findByName(anyString())).willReturn(Optional.of(gateToUpdate));
        given(flightRepository.findByCode(anyString())).willReturn(Optional.of(flight));

        GateResponseDto result = gateService.updateGate("gate", gateDto);

        assertEquals("Successfully updated gate gate", result.getResponseMessage());
        assertEquals(1L, result.getAssignedGate().getId());
        assertEquals(9L, result.getAssignedGate().getFlightDto().getId());
        assertEquals("FL9", result.getAssignedGate().getFlightDto().getCode());

        verify(gateRepository, times(1)).findByName(anyString());
        verify(flightRepository, times(1)).findByCode(anyString());
    }

    @Test
    @DisplayName("Update gate - bad gate name")
    public void testUpdateGate2() {
        var gateDto = new GateDto(1L, "g1", null);

        given(gateRepository.findByName(anyString())).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gateService.updateGate("gate", gateDto));

        verify(gateRepository, times(1)).findByName(anyString());
        verifyNoMoreInteractions(gateRepository);
        verifyNoInteractions(flightRepository);
    }

    @Test
    @DisplayName("Update gate - missing flight code")
    public void testUpdateGate3() {
        var gateDto = new GateDto(1L, "g1", null);
        var gateToUpdate = new Gate(2L, "g2", null);

        given(gateRepository.findByName(anyString())).willReturn(Optional.of(gateToUpdate));

        assertThrows(IllegalArgumentException.class, () -> gateService.updateGate("gate", gateDto));

        verify(gateRepository, times(1)).findByName(anyString());
        verifyNoMoreInteractions(gateRepository);
        verifyNoInteractions(flightRepository);
    }

    @Test
    @DisplayName("Update gate - bad flight code")
    public void testUpdateGate4() {
        var flightDto = new FlightDto(1L, "FL1", null, null);
        var gateDto = new GateDto(1L, "g1", flightDto);
        var gateToUpdate = new Gate(2L, "g2", null);

        given(gateRepository.findByName(anyString())).willReturn(Optional.of(gateToUpdate));
        given(flightRepository.findByCode(anyString())).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gateService.updateGate("gate", gateDto));

        verify(gateRepository, times(1)).findByName(anyString());
        verify(flightRepository, times(1)).findByCode(anyString());
        verifyNoMoreInteractions(gateRepository);
        verifyNoMoreInteractions(flightRepository);
    }

    private List<Gate> prepareGates() {
        return List.of(new Gate(1L, "g1", null),
                        new Gate(2L, "g2", null),
                        new Gate(3L, "g3", null));
    }

    private Flight prepareFlight() {
        var arrival = LocalDateTime.of(2021, 1, 12, 1, 0);
        var departure = LocalDateTime.of(2021, 1, 13, 14, 30);
        return new Flight(1L, "FL", arrival, departure, null);
    }

} 
