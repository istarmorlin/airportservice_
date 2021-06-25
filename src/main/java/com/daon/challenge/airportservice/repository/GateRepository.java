package com.daon.challenge.airportservice.repository;

import com.daon.challenge.airportservice.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {

    @Query("select g from Gate g where g.flight is null")
    List<Gate> findAvailableGates();

    Optional<Gate> findByName(String name);
}
