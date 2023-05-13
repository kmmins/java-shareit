package ru.practicum.shareit.repository;

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

    @BeforeEach
    public void addData() {
        UserEntity user1 = new UserEntity(1L, "user1", "user1@user.com");
        UserEntity user2 = new UserEntity(2L, "user2", "user2@user.com");
        UserEntity user3 = new UserEntity(3L, "user3", "user3@user.com");
        var user_Own1Item_Has3Bookings = userRepositoryJpa.save(user1);
        var user_Own2Items_Have2Bookings = userRepositoryJpa.save(user2);
        var booker = userRepositoryJpa.save(user3);
        ItemEntity itemA = new ItemEntity(1L);
        itemA.setName("A");
        itemA.setDescription("AAA");
        itemA.setAvailable(true);
        itemA.setOwnerId(user_Own1Item_Has3Bookings.getId());
        itemA.setRequestId(null);
        ItemEntity itemB = new ItemEntity(2L);
        itemB.setName("B");
        itemB.setDescription("BBB");
        itemB.setAvailable(true);
        itemB.setOwnerId(user_Own2Items_Have2Bookings.getId());
        itemB.setRequestId(null);
        ItemEntity itemC = new ItemEntity(3L);
        itemC.setName("C");
        itemC.setDescription("CCC");
        itemC.setAvailable(true);
        itemC.setOwnerId(user_Own2Items_Have2Bookings.getId());
        itemC.setRequestId(null);
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
        //data
        var allUsers = userRepositoryJpa.findAll();

        //test
        Query query = em.getEntityManager().createNativeQuery(
                        "select * " +
                        "from bookings " +
                        "where item_id in (select id " +
                        "from items " +
                        "where owner_id = ?1) " +
                        "order by start_time desc", BookingEntity.class
        );
        List<BookingEntity> actual = query.setParameter(1, allUsers.get(1))
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
        //data
        var allUsers = userRepositoryJpa.findAll();

        //test
        Query query = em.getEntityManager().createNativeQuery("select * " +
                "from bookings " +
                "where booker_id =?1 " +
                "order by start_time desc", BookingEntity.class
        );
        List<BookingEntity> actual = query.setParameter(1, allUsers.get(2)).getResultList();

        //assert
        assertEquals(5, actual.size(), "Ошибка в количестве бронирований букера.");
    }

    @Test
    public void checkFindAllByBookerIdPageNative_Ok() {
        //data
        var allUsers = userRepositoryJpa.findAll();

        //test
        Query query = em.getEntityManager().createNativeQuery("select * " +
                "from bookings " +
                "where booker_id =?1 " +
                "order by start_time desc", BookingEntity.class
        );
        List<BookingEntity> actual = query.setParameter(1, allUsers.get(2))
                .setFirstResult(0)
                .setMaxResults(3)
                .getResultList();

        //assert
        assertEquals(3, actual.size(), "Ошибка в количества бронирований букера," +
                " после ограничения количества элементов для отображения.");
    }
}
