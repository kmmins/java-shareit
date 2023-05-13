package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.*;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice(assignableTypes = BookingController.class)
public class BookingExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingParameter(final ParameterException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingNotValid(final ValidationException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingNotAvailable(final NotAvailableException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFound(final NotFoundException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotOwn(final NotOwnException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotOwnItemOrBooking(final NotOwnItemOrNotOwnBookingException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
