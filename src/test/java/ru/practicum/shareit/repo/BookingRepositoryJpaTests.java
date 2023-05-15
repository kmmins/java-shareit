package ru.practicum.shareit.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepositoryJpa;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import javax.persistence.Query;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookingRepositoryJpaTests {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookingRepositoryJpa bookingRepositoryJpa;
    @Autowired
    private ItemRepositoryJpa itemRepositoryJpa;
    @Autowired
    private UserRepositoryJpa userRepositoryJpa;

    private Long userOwn2ItemsHave2BookingsId;
    private Long bookerWhoHave5BookingsId;

    @BeforeEach
    public void addData() {
        UserEntity user1 = new UserEntity(1L, "user1", "user1@user.com");
        UserEntity user2 = new UserEntity(2L, "user2", "user2@user.com");
        UserEntity user3 = new UserEntity(3L, "user3", "user3@user.com");
        var userOwn1ItemHas3Bookings = userRepositoryJpa.save(user1);
        var userOwn2ItemsHave2Bookings = userRepositoryJpa.save(user2);
        var booker = userRepositoryJpa.save(user3);
        userOwn2ItemsHave2BookingsId = userOwn2ItemsHave2Bookings.getId();
        bookerWhoHave5BookingsId = booker.getId();
        var itemA = new ItemEntity(1L, "A", "AAA", true, userOwn1ItemHas3Bookings.getId(), null, null, null);
        var itemB = new ItemEntity(2L, "B", "BBB", true, userOwn2ItemsHave2Bookings.getId(), null, null, null);
        var itemC = new ItemEntity(3L, "C", "CCC", true, userOwn2ItemsHave2Bookings.getId(), null, null, null);
        var testItemA = itemRepositoryJpa.save(itemA);
        var testItemB = itemRepositoryJpa.save(itemB);
        var testItemC = itemRepositoryJpa.save(itemC);
        BookingEntity booking1 = new BookingEntity(1L);
        booking1.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        booking1.setEndTime(LocalDateTime.of(2000, 1, 1, 1, 2));
        booking1.setStatus(Status.APPROVED);
        booking1.setBooker(booker);
        booking1.setItem(testItemB);
        BookingEntity booking2 = new BookingEntity(2L);
        booking2.setStartTime(LocalDateTime.of(2111, 1, 1, 1, 1));
        booking2.setEndTime(LocalDateTime.of(2111, 1, 1, 1, 2));
        booking2.setStatus(Status.APPROVED);
        booking2.setBooker(booker);
        booking2.setItem(testItemC);
        BookingEntity booking3 = new BookingEntity(3L);
        booking3.setStartTime(LocalDateTime.of(2222, 1, 1, 1, 1));
        booking3.setEndTime(LocalDateTime.of(2222, 1, 1, 1, 2));
        booking3.setStatus(Status.APPROVED);
        booking3.setBooker(booker);
        booking3.setItem(testItemA);
        BookingEntity booking4 = new BookingEntity(4L);
        booking4.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        booking4.setEndTime(LocalDateTime.of(3000, 1, 1, 1, 2));
        booking4.setStatus(Status.CANCELED);
        booking4.setBooker(booker);
        booking4.setItem(testItemA);
        BookingEntity booking5 = new BookingEntity(5L);
        booking5.setStartTime(LocalDateTime.of(2022, 1, 1, 1, 1));
        booking5.setEndTime(LocalDateTime.of(2222, 1, 1, 1, 2));
        booking5.setStatus(Status.APPROVED);
        booking5.setBooker(booker);
        booking5.setItem(testItemA);
        bookingRepositoryJpa.save(booking1);
        bookingRepositoryJpa.save(booking2);
        bookingRepositoryJpa.save(booking3);
        bookingRepositoryJpa.save(booking4);
        bookingRepositoryJpa.save(booking5);
    }

    @Test
    public void checkFindAllForItemOwnByUserPage_Ok() {
        //test
        Query query = em.getEntityManager().createNativeQuery(
                        "select * " +
                        "from bookings " +
                        "where item_id in (select id " +
                        "from items " +
                        "where owner_id = ?1) " +
                        "order by start_time desc", BookingEntity.class
        );
        List<BookingEntity> actual = query.setParameter(1, userOwn2ItemsHave2BookingsId)
                .setFirstResult(0)
                .setMaxResults(20)
                .getResultList();

        //assert
        assertEquals(2, actual.size(), "Ошибка в количестве бронирований предметов, владельцем которых является юзер.");
        assertEquals("C", actual.get(0).getItem().getName(), "Ошибка в имени предмета, в найденных бронированиях.");
        assertEquals("BBB", actual.get(1).getItem().getDescription(), "Ошибка в описании предмета." +
                " в найденных бронированиях.");
    }

    @Test
    public void checkFindAllByBookerIdNative_Ok() {
        //test
        Query query = em.getEntityManager().createNativeQuery("select * " +
                "from bookings " +
                "where booker_id =?1 " +
                "order by start_time desc", BookingEntity.class
        );
        List<BookingEntity> actual = query.setParameter(1, bookerWhoHave5BookingsId).getResultList();

        //assert
        assertEquals(5, actual.size(), "Ошибка в количестве бронирований букера.");
    }

    @Test
    public void checkFindAllByBookerIdPageNative_Ok() {
        //test
        Query query = em.getEntityManager().createNativeQuery("select * " +
                "from bookings " +
                "where booker_id =?1 " +
                "order by start_time desc", BookingEntity.class
        );
        List<BookingEntity> actual = query.setParameter(1, bookerWhoHave5BookingsId)
                .setFirstResult(0)
                .setMaxResults(3)
                .getResultList();

        //assert
        assertEquals(3, actual.size(), "Ошибка в количества бронирований букера," +
                " после ограничения количества элементов для отображения.");
    }
}
