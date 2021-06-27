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
import java.time.LocalTime;
import java.util.ArrayList;
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
        var requestTime = LocalTime.of(10, 0);

        given(gateRepository.findAll()).willReturn(availableGates);
        given(flightRepository.findByCode(anyString())).willReturn(Optional.of(flight));

        GateResponseDto result = gateService.assignGate(flightDto, requestTime);

        assertEquals(1L, result.getAssignedGate().getId());
        assertEquals("Successfully assigned gate", result.getResponseMessage());

        verify(gateRepository, times(1)).findAll();
        verify(flightRepository, times(1)).findByCode(anyString());
    }

    @Test
    @DisplayName("Assign gate - no available gates")
    public void testAssignGate2() {
        List<Gate> availableGates = Collections.emptyList();
        var flight = prepareFlight();
        var flightDto = FlightMapper.toDto(flight);
        var requestTime = LocalTime.of(10, 0);

        given(gateRepository.findAll()).willReturn(availableGates);

        GateResponseDto result = gateService.assignGate(flightDto, requestTime);

        assertNull(result.getAssignedGate());
        assertEquals("No available gate at this time", result.getResponseMessage());

        verify(gateRepository, times(1)).findAll();
        verifyNoMoreInteractions(gateRepository);
        verifyNoInteractions(flightRepository);
    }

    @Test
    @DisplayName("Assign gate - bad flight code")
    public void testAssignGate3() {
        var availableGates = prepareGates();
        var flight = prepareFlight();
        var flightDto = FlightMapper.toDto(flight);
        var requestTime = LocalTime.of(10, 0);

        given(gateRepository.findAll()).willReturn(availableGates);
        given(flightRepository.findByCode(anyString())).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gateService.assignGate(flightDto, requestTime));

        verify(gateRepository, times(1)).findAll();
        verify(flightRepository, times(1)).findByCode(anyString());
        verifyNoMoreInteractions(gateRepository);
        verifyNoMoreInteractions(flightRepository);
    }

    @Test
    @DisplayName("Update gate flights - happy flow")
    public void testUpdateGateFlights() {
        var flightToUpdate = prepareFlight();
        var gateToUpdate = new Gate(1L, "g1", LocalTime.of(8, 0), LocalTime.of(20, 0), List.of(flightToUpdate));
        var gateDto = new GateDto(3L, "g3", null, null, List.of(new FlightDto(3L, "FL3", null, null)));
        var flight = new Flight(9L, "FL9", null, null, null);

        given(gateRepository.findByName(anyString())).willReturn(Optional.of(gateToUpdate));
        given(flightRepository.findByCode(anyString())).willReturn(Optional.of(flight));

        GateResponseDto result = gateService.updateGateFlights("gate", gateDto);

        assertEquals("Successfully updated gate gate", result.getResponseMessage());
        assertEquals(1L, result.getAssignedGate().getId());
        assertEquals(9L, result.getAssignedGate().getFlightDtos().get(0).getId());
        assertEquals("FL9", result.getAssignedGate().getFlightDtos().get(0).getCode());

        verify(gateRepository, times(1)).findByName(anyString());
        verify(flightRepository, times(1)).findByCode(anyString());
    }

    @Test
    @DisplayName("Update gate flights - bad gate name")
    public void testUpdateGateFlights2() {
        var gateDto = new GateDto(1L, "g1", null, null,null);

        given(gateRepository.findByName(anyString())).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gateService.updateGateFlights("gate", gateDto));

        verify(gateRepository, times(1)).findByName(anyString());
        verifyNoMoreInteractions(gateRepository);
        verifyNoInteractions(flightRepository);
    }

    @Test
    @DisplayName("Update gate flights - missing flight code")
    public void testUpdateGateFlights3() {
        var gateDto = new GateDto(1L, "g1", null, null, null);
        var gateToUpdate = new Gate(1L, "g1", LocalTime.of(8, 0), LocalTime.of(20, 0), null);

        given(gateRepository.findByName(anyString())).willReturn(Optional.of(gateToUpdate));

        assertThrows(IllegalArgumentException.class, () -> gateService.updateGateFlights("gate", gateDto));

        verify(gateRepository, times(1)).findByName(anyString());
        verifyNoMoreInteractions(gateRepository);
        verifyNoInteractions(flightRepository);
    }

    @Test
    @DisplayName("Update gate flights - bad flight code")
    public void testUpdateGateFlights4() {
        var flightDto = new FlightDto(1L, "FL1", null, null);
        var gateDto = new GateDto(1L, "g1", null, null, List.of(flightDto));
        var gateToUpdate = new Gate(1L, "g1", LocalTime.of(8, 0), LocalTime.of(20, 0), null);

        given(gateRepository.findByName(anyString())).willReturn(Optional.of(gateToUpdate));
        given(flightRepository.findByCode(anyString())).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gateService.updateGateFlights("gate", gateDto));

        verify(gateRepository, times(1)).findByName(anyString());
        verify(flightRepository, times(1)).findByCode(anyString());
        verifyNoMoreInteractions(gateRepository);
        verifyNoMoreInteractions(flightRepository);
    }

    private List<Gate> prepareGates() {
        return List.of(new Gate(1L, "g1", LocalTime.of(8, 0), LocalTime.of(20, 0), new ArrayList<>()),
                        new Gate(2L, "g2", LocalTime.of(8, 0), LocalTime.of(20, 0), new ArrayList<>()),
                        new Gate(3L, "g3", LocalTime.of(8, 0), LocalTime.of(20, 0), new ArrayList<>()));
    }

    private Flight prepareFlight() {
        var arrival = LocalDateTime.of(2021, 1, 12, 1, 0);
        var departure = LocalDateTime.of(2021, 1, 13, 14, 30);
        return new Flight(1L, "FL", arrival, departure, null);
    }

} 
