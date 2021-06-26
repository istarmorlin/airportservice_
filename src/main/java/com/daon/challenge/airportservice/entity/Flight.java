package com.daon.challenge.airportservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private LocalDateTime arrival;

    private LocalDateTime nextDeparture;

    @OneToOne(mappedBy = "flight", cascade = CascadeType.ALL)
    private Gate assignedGate;
}
