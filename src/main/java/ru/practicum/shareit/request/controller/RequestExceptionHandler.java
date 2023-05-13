package ru.practicum.shareit.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterException;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice(assignableTypes = RequestController.class)
public class RequestExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestNotValid(final ValidationException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestParameter(final ParameterException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRequestFound(final NotFoundException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
