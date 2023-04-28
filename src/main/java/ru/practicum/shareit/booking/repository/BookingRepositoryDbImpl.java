package ru.practicum.shareit.booking.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.BookingEntity;

import java.util.List;

@Repository
public class BookingRepositoryDbImpl implements BookingRepository {

    private final BookingRepositoryJpa bookingRepositoryJpa;

    @Autowired
    public BookingRepositoryDbImpl(BookingRepositoryJpa bookingRepositoryJpa) {
        this.bookingRepositoryJpa = bookingRepositoryJpa;
    }

    @Override
    public BookingEntity add(BookingEntity booking) {
        return bookingRepositoryJpa.save(booking);
    }

    @Override
    public BookingEntity update(BookingEntity booking) {
        return bookingRepositoryJpa.save(booking);
    }

    @Override
    public BookingEntity getById(Long bookingId) {
        return bookingRepositoryJpa.findById(bookingId).orElse(null);
    }

    @Override
    public List<BookingEntity> getAllForUser(Long userId) {
        return bookingRepositoryJpa.findAllByBookerIdOrderByStartTimeDesc(userId);
    }

    @Override
    public List<BookingEntity> getAllForOwnerItems(Long userId) {
        return bookingRepositoryJpa.findAllForItemOwnByUser(userId);
    }
}
