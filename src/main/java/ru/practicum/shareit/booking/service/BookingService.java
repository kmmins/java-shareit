package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    /**
     * Сервисная обработка запроса на добавление нового бронирования.
     *
     * @param userId     id пользователя.
     * @param BookingDto бронирование (внешнее представление).
     * @return бронирование (внешнее представление).
     */
    BookingDto add(Long userId, BookingDto BookingDto);

    /**
     * Сервисная обработка запроса на подтверждение или отклонение запроса на бронирование.
     *
     * @param userId    id пользователя.
     * @param bookingId id бронирования.
     * @param approved  подтверждение (да/нет).
     * @return обновленное бронирование (внешнее представление).
     */
    BookingDto approve(Long userId, Long bookingId, boolean approved);

    /**
     * Сервисная обработка запроса на получение данных о конкретном бронировании.
     *
     * @param userId    id пользователя.
     * @param bookingId id бронирования.
     * @return бронирование (внешнее представление).
     */
    BookingDto getById(Long userId, Long bookingId);

    /**
     * Получение списка всех бронирований текущего пользователя.
     *
     * @param userId id пользователя.
     * @param state  параметр отображения бронирований.
     * @return списка всех бронирований (внешнее представление) пользователя.
     */
    List<BookingDto> getAllBookingForUser(Long userId, State state);

    /**
     * Получение списка всех бронирований для всех вещей текущего пользователя.
     *
     * @param userId id пользователя.
     * @param state  параметр отображения бронирований.
     * @return списка всех бронирований для всех вещей пользователя.
     */
    List<BookingDto> getAllBookingForOwnerItems(Long userId, State state);
}
