package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ParameterException;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(@Qualifier("bookingServiceImpl") BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        var addedBookingDto = bookingService.add(userId, bookingDto);
        log.info("Обработка запроса POST /bookings. Бронь добавлена: {}.", addedBookingDto);
        return addedBookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto ApproveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam boolean approved) {
        var approvedOrNotBookingDto = bookingService.approve(userId, bookingId, approved);
        log.info("Обработка запроса PATCH /bookings/{bookingId}?approved={approved}. Изменения брони: {}.", bookingId);
        return approvedOrNotBookingDto;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getByIdBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId) {
        var bookingGetByIdDto = bookingService.getById(userId, bookingId);
        log.info("Обработка запроса GET /bookings/{bookingId}. Получены данные о брони: {},", bookingId);
        return bookingGetByIdDto;
    }

    @GetMapping
    public List<BookingDto> getAllBookingsForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(required = false, defaultValue = "ALL") String state) {
        var allBookingDtoForUser = bookingService.getAllBookingForUser(userId, parseEnum(state));
        log.info("Обработка запроса GET /bookings?state={state}. Получены все брони пользователя: {}.", userId);
        return allBookingDtoForUser;
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(required = false, defaultValue = "ALL") String state) {
        var allBookingDtoForOwnerItems = bookingService.getAllBookingForOwnerItems(userId, parseEnum(state));
        log.info("Обработка запроса GET /bookings/owner?state={state}. Получены брони вещей пользователя: {}.", userId);
        return allBookingDtoForOwnerItems;
    }

    private State parseEnum(String state) {
        State stateEnum;
        try {
            stateEnum = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new ParameterException("Unknown state: " + state);
        }
        return stateEnum;
    }
}
