package ru.practicum.shareit.exception;

public class NotOwnOrCompleteThisBookingException extends RuntimeException {

    public NotOwnOrCompleteThisBookingException(String message) {
        super(message);
    }
}
