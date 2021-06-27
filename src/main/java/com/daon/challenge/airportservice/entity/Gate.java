package com.daon.challenge.airportservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gate {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private LocalTime openingTime;

    private LocalTime closingTime;

    @OneToMany(mappedBy = "assignedGate", cascade = CascadeType.ALL)
    private List<Flight> flights;
}
