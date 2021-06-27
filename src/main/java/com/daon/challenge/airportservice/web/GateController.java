package com.daon.challenge.airportservice.web;

import com.daon.challenge.airportservice.dto.FlightDto;
import com.daon.challenge.airportservice.dto.GateDto;
import com.daon.challenge.airportservice.dto.GateResponseDto;
import com.daon.challenge.airportservice.service.GateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/gates")
@RequiredArgsConstructor
public class GateController {

    private final GateService gateService;

    @GetMapping
    public List<GateDto> retrieveAllGates() {
        return gateService.retrieveAllGates();
    }

    @PostMapping
    public GateResponseDto assignGate(@RequestBody FlightDto flightDto) {
        return gateService.assignGate(flightDto, LocalTime.now());
    }

    @PatchMapping("/{gateName}")
    public GateResponseDto updateGate(@PathVariable("gateName") String gateName, @RequestBody GateDto gateDto) {
        return gateService.updateGate(gateName, gateDto);
    }
}
