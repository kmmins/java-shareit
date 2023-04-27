package ru.practicum.shareit.exception;

public class NotOwnItemOrNotOwnBookingException extends RuntimeException {

    public NotOwnItemOrNotOwnBookingException(String message) {
        super(message);
    }
}
