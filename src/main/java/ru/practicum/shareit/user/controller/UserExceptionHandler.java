package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.user.exception.UserAlreadyExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserParameterException;
import ru.practicum.shareit.user.model.UserErrorResponse;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserErrorResponse handleUserParameter(final UserParameterException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new UserErrorResponse(
                e.getMessage()
        );
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserErrorResponse handleUserNotValid(final ValidationException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new UserErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserErrorResponse handleUserNotFound(final UserNotFoundException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new UserErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public UserErrorResponse handleUserAlreadyExist(final UserAlreadyExistException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new UserErrorResponse(
                e.getMessage()
        );
    }
}
