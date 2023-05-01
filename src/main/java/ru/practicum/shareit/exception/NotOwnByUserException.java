package ru.practicum.shareit.exception;

public class NotOwnByUserException extends RuntimeException {

    public NotOwnByUserException(String message) {
        super(message);
    }
}
