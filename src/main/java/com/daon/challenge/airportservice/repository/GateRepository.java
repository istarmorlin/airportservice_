package com.daon.challenge.airportservice.repository;

import com.daon.challenge.airportservice.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {

    Optional<Gate> findByName(String name);
}
