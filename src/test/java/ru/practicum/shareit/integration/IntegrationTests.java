package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnOrCompleteThisBookingException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class IntegrationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private RequestService requestService;

    @Test
    public void checkUserGetById() {
    }

    @Test
    public void checkItemsGetAllItemsByOwnerId_Ok() {
        //data
        UserDto user1 = new UserDto();
        user1.setName("Max");
        user1.setEmail("max@rock.com");
        var max = userService.add(user1);
        ItemDto item1 = new ItemDto();
        item1.setName("itemMax");
        item1.setDescription("itemMax_Description");
        item1.setAvailable(true);
        itemService.add(max.getId(), item1);

        //test
        var result = itemService.getAllItemsByOwnerId(max.getId(), 0, 20);

        //assert
        assertEquals(1, result.size(), "Ошибка, у Макса не предметов.");
        assertEquals("itemMax", result.get(0).getName(), "Ошибка в имени.");
        assertEquals("itemMax_Description", result.get(0).getDescription(), "Ошибка в описании.");
    }

    @Test
    public void checkBookingApprove_Ok() {
        //data
        UserDto user1 = new UserDto();
        user1.setName("Max");
        user1.setEmail("max@rock.com");
        var max = userService.add(user1);
        UserDto user2 = new UserDto();
        user2.setName("Den");
        user2.setEmail("Den@booker.com");
        var den = userService.add(user2);
        ItemDto item1 = new ItemDto();
        item1.setName("itemMax");
        item1.setDescription("itemMax_Description");
        item1.setAvailable(true);
        var maxItem = itemService.add(max.getId(), item1);
        BookingDto booking1 = new BookingDto();
        booking1.setItemId(maxItem.getId());
        booking1.setStart(LocalDateTime.of(2023, 9, 1, 12, 0));
        booking1.setEnd(LocalDateTime.of(2023, 10, 1, 12, 0));
        var bookingDen = bookingService.add(den.getId(), booking1);

        //test
        var allMaxBooking = bookingService.getAllBookingForOwnerItems(max.getId(), State.ALL, 0, 20);
        assertEquals(Status.WAITING, allMaxBooking.get(0).getStatus());
        var result = bookingService.approve(max.getId(), bookingDen.getId(), true);

        //assert
        assertEquals(Status.APPROVED, result.getStatus(), "Ошибка, статус брони не подтвержден.");
    }

    @Test
    public void check_AddComment_Throw404() {
        //data
        UserDto user1 = new UserDto();
        user1.setName("Max");
        user1.setEmail("max@rock.com");
        var max = userService.add(user1);
        UserDto user2 = new UserDto();
        user2.setName("Den");
        user2.setEmail("Den@booker.com");
        var den = userService.add(user2);
        ItemDto item1 = new ItemDto();
        item1.setName("itemMax");
        item1.setDescription("itemMax_Description");
        item1.setAvailable(true);
        var maxItem = itemService.add(max.getId(), item1);
        Long wrongItemId = 100500L;
        CommentDto denComment = new CommentDto();
        denComment.setText("Good!");

        //test
        final NotFoundException e1 = assertThrows(NotFoundException.class,
                () -> itemService.addComment(den.getId(), wrongItemId, denComment));
        final NotOwnOrCompleteThisBookingException e2 = assertThrows(NotOwnOrCompleteThisBookingException.class,
                () -> itemService.addComment(den.getId(), maxItem.getId(), denComment));

        //assert
        assertEquals(String.format("Не найден предмет с id %d.", wrongItemId), e1.getMessage());
        assertEquals("Пользователь не арендовал предмет или не завершил аренду.", e2.getMessage());
    }

    @Test
    public void checkRequestGetAllFromSize_Ok() {
        //data
        UserDto user1 = new UserDto();
        user1.setName("Max");
        user1.setEmail("max@rock.com");
        var max = userService.add(user1);
        UserDto user2 = new UserDto();
        user2.setName("Ana");
        user2.setEmail("Ana@requestor.com");
        var ana = userService.add(user2);
        UserDto user3 = new UserDto();
        user3.setName("Aren");
        user3.setEmail("Aren@requestor.com");
        var aren = userService.add(user3);
        UserDto user4 = new UserDto();
        user4.setName("Alex");
        user4.setEmail("max@requestor.com");
        var alex = userService.add(user4);
        RequestDto request1 = new RequestDto();
        request1.setDescription("anaNeed");
        RequestDto request2 = new RequestDto();
        request2.setDescription("arenNeed");
        RequestDto request3 = new RequestDto();
        request3.setDescription("alexNeed");
        requestService.add(ana.getId(), request1);
        requestService.add(aren.getId(), request2);
        requestService.add(alex.getId(), request3);

        //test
        var result = requestService.getAllFromSize(max.getId(), 0, 20);

        //assert
        assertEquals(3, result.size(), "Ошибка количества реквестов.");
        assertEquals(alex.getId(), result.get(0).getRequestOwner(), "Ошибкаб у Алекса не самый последний реквест.");
    }
}
