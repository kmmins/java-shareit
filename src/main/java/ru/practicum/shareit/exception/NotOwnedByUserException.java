package ru.practicum.shareit.exception;

public class NotOwnedByUserException extends RuntimeException {

    public NotOwnedByUserException(String message) {
        super(message);
    }
}
