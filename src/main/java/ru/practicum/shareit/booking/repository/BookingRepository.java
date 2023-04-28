package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.BookingEntity;

import java.util.List;

public interface BookingRepository {

    /**
     * Добавление нового бронирования в хранилище.
     *
     * @param booking бронирование (внутреннее представление).
     * @return добавленное бронирование (внутреннее представление).
     */
    BookingEntity add(BookingEntity booking);

    /**
     * Сохранение в хранилище подтверждения или отклонения запроса на бронирование;
     *
     * @param booking бронирование (внутреннее представление).
     * @return обновленное бронирование (внутреннее представление).
     */
    BookingEntity update(BookingEntity booking);

    /**
     * Получение данных о конкретном бронировании из хранилища.
     *
     * @param bookingId id бронирования (внутреннее представление).
     * @return найденное бронирование (внутреннее представление).
     */
    BookingEntity getById(Long bookingId);

    /**
     * Получение списка всех бронирований конкретного пользователя.
     *
     * @param userId id пользователя, для которого нужно найти все бронирования.
     * @return список всех бронирований пользователя.
     */
    List<BookingEntity> getAllForUser(Long userId);

    /**
     * Получение списка бронирований для всех вещей конкретного пользователя.
     *
     * @param userId id пользователя - владельца вещей, для которых нужно найти все бронирования.
     * @return списка бронирований для всех вещей пользователя.
     */
    List<BookingEntity> getAllForOwnerItems(Long userId);
}
