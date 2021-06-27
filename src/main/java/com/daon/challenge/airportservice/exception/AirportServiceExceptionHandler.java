package com.daon.challenge.airportservice.exception;

import com.daon.challenge.airportservice.dto.GateResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class AirportServiceExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GateResponseDto handleBadRequestException(RuntimeException exception) {
        return new GateResponseDto(null, exception.getMessage());
    }

    @ExceptionHandler(value = UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GateResponseDto handleUnsupportedOperationException(RuntimeException exception) {
        return new GateResponseDto(null, exception.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public IllegalArgumentException handleException(RuntimeException exception) {
        throw new IllegalArgumentException(exception.getMessage());
    }
}
