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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public GateResponseDto assignGate(FlightDto flightDto) {
        var availableGates = gateRepository.findAvailableGates();
        if (availableGates.isEmpty()) {
            return new GateResponseDto(null, "No available gate at this time");
        }

        Flight flight = flightRepository.findByCode(flightDto.getCode())
                                        .orElseThrow(() -> new IllegalArgumentException("No flight with code " + flightDto.getCode()));
        Gate availableGate = chooseGate(availableGates);
        availableGate.setFlight(flight);

        return new GateResponseDto(GateMapper.toDto(availableGate), "Successfully assigned gate");
    }

    @Transactional
    public GateResponseDto updateGate(String gateName, GateDto gateDto) {
        Gate gateToUpdate = gateRepository.findByName(gateName)
                                            .orElseThrow(() -> new IllegalArgumentException("No gate named " + gateName));

        String flightCode = extractFlightCode(gateDto);
        Flight flight = flightRepository.findByCode(flightCode)
                                        .orElseThrow(() -> new IllegalArgumentException("No flight with code " + flightCode));
        gateToUpdate.setFlight(flight);

        return new GateResponseDto(GateMapper.toDto(gateToUpdate), "Successfully updated gate " + gateName);
    }

    private Gate chooseGate(List<Gate> availableGates) {
        return availableGates.size() > 0 ? availableGates.get(0) : new Gate();
    }

    private String extractFlightCode(GateDto gateDto) {
        return Optional.ofNullable(gateDto.getFlightDto())
                        .map(FlightDto::getCode)
                        .orElseThrow(() -> new IllegalArgumentException("Flight code missing"));
    }
}
