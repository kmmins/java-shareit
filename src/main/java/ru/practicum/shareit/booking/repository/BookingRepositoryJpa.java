package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.shareit.booking.model.BookingEntity;

import java.util.List;

@EnableJpaRepositories
public interface BookingRepositoryJpa extends JpaRepository<BookingEntity, Long> {

    @Query(value = "select * " +
            "from bookings " +
            "where booker_id =?1 " +
            "order by start_time desc",
            nativeQuery = true)
    List<BookingEntity> findAllByBookerId(Long bookerId);

    @Query(value = "select * " +
            "from bookings " +
            "where booker_id =?1 " +
            "order by start_time desc",
            nativeQuery = true)
    Page<BookingEntity> findAllByBookerIdPage(Long bookerId, PageRequest pageRequest);

    @Query(value = "select * " +
            "from bookings " +
            "where item_id in (select id " +
            "from items " +
            "where owner_id = ?1) " +
            "order by start_time desc",
            nativeQuery = true)
    Page<BookingEntity> findAllForItemOwnByUser(Long userId, PageRequest pageRequest);
}
