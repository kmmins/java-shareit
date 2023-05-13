package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterException;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserParameter(final ParameterException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserNotValid(final ValidationException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final NotFoundException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserNotUniq(final DataIntegrityViolationException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserEmailAlreadyExist(final AlreadyExistException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
