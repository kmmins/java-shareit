package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.shareit.booking.model.BookingEntity;

import java.util.List;

@EnableJpaRepositories
public interface BookingRepositoryJpa extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findAllByBookerIdOrderByStartTimeDesc(Long bookerId);

    @Query(value = "select * " +
            "from bookings as b " +
            "where b.item_id in (select i.id " +
            "from items as i " +
            "where i.owner_id = ?1) " +
            "order by b.start_time desc",
            nativeQuery = true)
    List<BookingEntity> findAllForItemOwnByUser(Long userId);
}
