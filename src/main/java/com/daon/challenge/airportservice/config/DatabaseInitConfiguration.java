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
import java.time.LocalTime;
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

            Stream.of(new Gate(null, "A1", LocalTime.of(8, 0), LocalTime.of(17, 0), null),
                        new Gate(null, "A2", LocalTime.of(6, 0), LocalTime.of(22, 0),null),
                        new Gate(null, "B1", LocalTime.of(3, 0), LocalTime.of(23, 45),null),
                        new Gate(null, "C1", LocalTime.of(0, 1), LocalTime.of(23, 59),null))
                    .forEach(gateRepository::save);
        };
    }
}
