package com.daon.challenge.airportservice.config;

import com.daon.challenge.airportservice.entity.Flight;
import com.daon.challenge.airportservice.entity.Gate;
import com.daon.challenge.airportservice.repository.FlightRepository;
import com.daon.challenge.airportservice.repository.GateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Configuration
public class DatabaseInitConfiguration {

    @Bean
    @Profile("dev")
    public CommandLineRunner initDbRunner(GateRepository gateRepository, FlightRepository flightRepository) {
        return arg -> {
            flightRepository.deleteAll();
            gateRepository.deleteAll();

            Stream.of(new Flight(null, "FL1", LocalDateTime.of(2021, 6, 25, 5, 31), LocalDateTime.of(2021, 6, 25, 12, 0), null),
                    new Flight(null, "FL2", LocalDateTime.of(2021, 6, 25, 5, 31), LocalDateTime.of(2021, 6, 25, 12, 0), null))
                    .forEach(flightRepository::save);

            Stream.of(new Gate(null, "A1", null),
                        new Gate(null, "A2", null),
                        new Gate(null, "B1", null),
                        new Gate(null, "C1", null))
                    .forEach(gateRepository::save);
        };
    }
}
