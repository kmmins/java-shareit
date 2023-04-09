package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotOwnedByUserException;
import ru.practicum.shareit.item.exception.ItemParameterException;
import ru.practicum.shareit.item.model.ItemErrorResponse;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class ItemExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemErrorResponse handleItemParameter(final ItemParameterException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ItemErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemErrorResponse handleItemNotValid(final ValidationException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ItemErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ItemErrorResponse handleItemNotFound(final ItemNotFoundException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ItemErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ItemErrorResponse handleItemNotOwned(final ItemNotOwnedByUserException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return new ItemErrorResponse(
                e.getMessage()
        );
    }
}
