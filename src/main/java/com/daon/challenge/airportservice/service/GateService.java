package com.daon.challenge.airportservice.service;

import com.daon.challenge.airportservice.dto.FlightDto;
import com.daon.challenge.airportservice.dto.GateDto;
import com.daon.challenge.airportservice.dto.GateResponseDto;
import com.daon.challenge.airportservice.entity.Flight;
import com.daon.challenge.airportservice.entity.Gate;
import com.daon.challenge.airportservice.repository.FlightRepository;
import com.daon.challenge.airportservice.repository.GateRepository;
import com.daon.challenge.airportservice.util.GateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GateService {

    private final GateRepository gateRepository;
    private final FlightRepository flightRepository;

    public List<GateDto> retrieveAllGates() {
        return gateRepository.findAll().stream()
                                        .map(GateMapper::toDto)
                                        .collect(Collectors.toList());
    }

    @Transactional
    public GateResponseDto assignGate(FlightDto flightDto, LocalTime time) {
        var availableGates =
                gateRepository.findAll().stream()
                                        .filter(gate -> isGateOpen(gate, time) && isGateAvailable(gate, flightDto))
                                        .collect(Collectors.toList());
        if (availableGates.isEmpty()) {
            return new GateResponseDto(null, "No available gate at this time");
        }

        Flight flight = flightRepository.findByCode(flightDto.getCode())
                                        .orElseThrow(() -> new IllegalArgumentException("No flight with code " + flightDto.getCode()));
        Gate availableGate = chooseGate(availableGates);
        flight.setAssignedGate(availableGate);
        availableGate.getFlights().add(flight);

        return new GateResponseDto(GateMapper.toDto(availableGate), "Successfully assigned gate");
    }

    @Transactional
    public GateResponseDto updateGate(String gateName, GateDto gateDto) {
        Gate gateToUpdate = gateRepository.findByName(gateName)
                                            .orElseThrow(() -> new IllegalArgumentException("No gate named " + gateName));

        var flights = extractFlightCodes(gateDto).stream()
                                                .map(code -> Pair.of(code, flightRepository.findByCode(code)))
                                                .map(pair ->
                                                        pair.getSecond().orElseThrow(() ->
                                                                new IllegalArgumentException("No flight with code " + pair.getFirst())))
                                                .collect(Collectors.toList());

        flights.forEach(f -> f.setAssignedGate(gateToUpdate));
        gateToUpdate.setFlights(flights);

        return new GateResponseDto(GateMapper.toDto(gateToUpdate), "Successfully updated gate " + gateName);
    }

    private Gate chooseGate(List<Gate> availableGates) {
        return availableGates.size() > 0 ? availableGates.get(0) : new Gate();
    }

    private List<String> extractFlightCodes(GateDto gateDto) {
        return Optional.ofNullable(gateDto.getFlightDtos())
                        .map(this::extractFlightCodes)
                        .orElseThrow(() -> new IllegalArgumentException("Flight code missing"));
    }

    private List<String> extractFlightCodes(List<FlightDto> flightDtos) {
        return flightDtos.stream()
                        .map(FlightDto::getCode)
                        .collect(Collectors.toList());
    }

    private boolean isGateOpen(Gate gate, LocalTime time) {
        return gate.getOpeningTime().isBefore(time) && gate.getClosingTime().isAfter(time);
    }

    private boolean isGateAvailable(Gate gate, FlightDto flightDto) {
        return gate.getFlights() == null
                || gate.getFlights().isEmpty()
                || gate.getFlights().stream()
                                    .anyMatch(flight -> flight.getNextDeparture().isBefore(flightDto.getArrival())
                                                    || flight.getArrival().isAfter(flightDto.getNextDeparture()));
    }
}
