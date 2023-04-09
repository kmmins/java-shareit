package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Booking {
    private final int id;
    private Integer bookingItemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer bookerId;
    private Status status;

    private enum Status {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }
}
