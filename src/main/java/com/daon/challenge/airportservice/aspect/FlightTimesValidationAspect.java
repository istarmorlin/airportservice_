package com.daon.challenge.airportservice.aspect;

import com.daon.challenge.airportservice.dto.FlightDto;
import com.daon.challenge.airportservice.dto.GateDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class FlightTimesValidationAspect {


    @Pointcut("execution(* com.daon.challenge.airportservice.service.GateService.assignGate(..))")
    public void executeOnAssign() {

    }

    @Pointcut("execution(* com.daon.challenge.airportservice.service.GateService.updateGate(..))")
    public void executeOnUpdate() {

    }

    @Before("executeOnAssign() || executeOnUpdate()")
    public void validateTimes(JoinPoint joinPoint) {
        var args = joinPoint.getArgs();

        for (Object arg: args) {
            if (arg instanceof FlightDto) {
                var flightDto = (FlightDto) arg;
                if (areInvalidTimes(flightDto)) {
                    throw new UnsupportedOperationException("Flight arrival time must be earlier than its departure time for flight " + flightDto.getCode());
                }
            }

            if (arg instanceof GateDto) {
                var gateDto = (GateDto) arg;

                for (FlightDto flightDto: gateDto.getFlightDtos()) {
                    if (areInvalidTimes(flightDto)) {
                        throw new UnsupportedOperationException("Flight arrival time must be earlier than its departure time for flight " + flightDto.getCode());
                    }
                }
            }
        }
    }

    private boolean areInvalidTimes(FlightDto flightDto) {
        return flightDto.getArrival().isAfter(flightDto.getNextDeparture());
    }
}
