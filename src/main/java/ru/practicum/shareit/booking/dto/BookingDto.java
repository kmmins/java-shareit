package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long id;
    @NotNull(message = "не должно быть null.")
    private Long itemId;
    private Long bookerId;
    @NotNull(message = "не должно быть null.")
    private LocalDateTime start;
    @NotNull(message = "не должно быть null.")
    private LocalDateTime end;
    private UserDto booker;
    private ItemDto item;
    private Status status;
}
