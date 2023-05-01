package ru.practicum.shareit.exception;

public class NotOwnException extends RuntimeException {

    public NotOwnException(String message) {
        super(message);
    }
}
