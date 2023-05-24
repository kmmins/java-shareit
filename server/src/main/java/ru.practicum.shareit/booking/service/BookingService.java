package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    /**
     * Сервисная обработка запроса на добавление нового бронирования.
     * Запрос может быть создан любым пользователем.
     *
     * @param userId     id пользователя.
     * @param bookingDto бронирование (Dto).
     * @return бронирование (Dto).
     */
    BookingDto add(Long userId, BookingDto bookingDto);

    /**
     * Сервисная обработка запроса на подтверждение или отклонение запроса на бронирование.
     * Может быть выполнено только владельцем вещи.
     *
     * @param userId    id пользователя.
     * @param bookingId id бронирования.
     * @param approved  подтверждение (да/нет).
     * @return обновленное бронирование (Dto).
     */
    BookingDto approve(Long userId, Long bookingId, boolean approved);

    /**
     * Сервисная обработка запроса на получение данных о конкретном бронировании.
     * Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
     *
     * @param userId    id пользователя.
     * @param bookingId id бронирования.
     * @return бронирование (Dto).
     */
    BookingDto getById(Long userId, Long bookingId);

    /**
     * Получение списка всех бронирований текущего пользователя.
     *
     * @param userId id пользователя.
     * @param state  параметр отображения бронирований.
     * @return списка всех бронирований (Dto) пользователя.
     */
    List<BookingDto> getAllBookingForUser(Long userId, State state, int from, int size);

    /**
     * Получение списка всех бронирований для всех вещей текущего пользователя.
     *
     * @param userId id пользователя.
     * @param state  параметр отображения бронирований.
     * @return списка всех бронирований для всех вещей пользователя.
     */
    List<BookingDto> getAllBookingForOwnerItems(Long userId, State state, int from, int size);
}
