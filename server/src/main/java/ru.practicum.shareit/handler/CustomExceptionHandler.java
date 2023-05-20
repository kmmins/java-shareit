package ru.practicum.shareit.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.*;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameter(final ParameterException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotAvailable(final NotAvailableException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotOwnOrComplete(final NotOwnOrCompleteThisBookingException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotOwnItemOrBooking(final NotOwnItemOrNotOwnBookingException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotOwned(final NotOwnException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExist(final AlreadyExistException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotUniq(final DataIntegrityViolationException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
