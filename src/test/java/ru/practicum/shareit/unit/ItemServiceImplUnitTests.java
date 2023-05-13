package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepositoryJpa;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnException;
import ru.practicum.shareit.exception.NotOwnOrCompleteThisBookingException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.CommentEntity;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.CommentRepositoryJpa;
import ru.practicum.shareit.item.repository.ItemRepositoryDbImpl;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.PageHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplUnitTests {

    @InjectMocks
    ItemServiceImpl itemService;
    @Mock
    ItemRepositoryDbImpl itemRepository;
    @Mock
    BookingRepositoryJpa bookingRepositoryJpa;
    @Mock
    CommentRepositoryJpa commentRepositoryJpa;
    @Mock
    UserService userService;

    @Test
    public void checkItemAdd_UserNotFoundAndIdOk() {
        //data
        Long userId = 1L;
        Long wrongUserId = 666L;
        Long itemId = 100500L;
        var itemDto = new ItemDto();
        itemDto.setName("A");
        itemDto.setDescription("AAA");
        itemDto.setAvailable(true);
        var itemModel = ItemMapper.convertToModel(userId, itemDto);
        var itemModeLAfter = new ItemEntity(
                itemId,
                itemDto.getName(),
                itemDto.getDescription(),
                true,
                userId,
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
        when(userService.getById(wrongUserId))
                .thenThrow(new NotFoundException(String.format("Не найден пользователь с id: %d.", wrongUserId)));
        when(itemRepository.add(itemModel))
                .thenReturn(itemModeLAfter);

        //test
        final NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.add(wrongUserId, itemDto));
        var check = itemService.add(userId, itemDto);

        //assert
        assertEquals(String.format("Не найден пользователь с id: %d.", wrongUserId), e.getMessage());
        assertEquals(itemId, check.getId(), "Некорректный id предмета." + check.getId());
    }

    @Test
    public void checkGetAllItemsByOwnerId_SizeOk() {
        //data
        Long userId = 1L;
        PageRequest pageRequest = PageHelper.createRequest(0, 20);
        var one = new ItemEntity();
        var two = new ItemEntity();
        var three = new ItemEntity();
        List<ItemEntity> list = List.of(one, two, three);
        Page<ItemEntity> page = new PageImpl<>(list);
        when(itemRepository.getAllItemsByOwnerId(userId, pageRequest))
                .thenReturn(page);

        //test
        var check = itemService.getAllItemsByOwnerId(userId, 0, 20);

        //assert
        assertEquals(3, check.size());
    }

    @Test
    public void checkItemGetById_ItemNotFound() {
        //data
        Long userId = 1L;
        Long itemId = 100500L;
        when(itemRepository.getById(Mockito.anyLong()))
                .thenReturn(null);

        //test
        final NotFoundException e = assertThrows(NotFoundException.class,
                () -> itemService.getById(userId, itemId));

        //assert
        assertEquals(String.format("Не найден предмет с id %d.", itemId), e.getMessage());
    }

    @Test
    public void checkItemGetById_OwnerLastNextIdOk() {
        //data
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 100500L;
        Long oneId = 1L;
        Long twoId = 2L;
        Long threeId = 3L;
        var booker = new UserEntity(bookerId, "booker", "booker@booker.com");
        ItemDto itemDto = new ItemDto();
        itemDto.setName("A");
        itemDto.setDescription("AAA");
        itemDto.setAvailable(true);
        var itemModel = new ItemEntity(itemId);
        itemModel.setName("A");
        itemModel.setDescription("AAA");
        itemModel.setAvailable(true);
        itemModel.setOwnerId(userId);
        var one = new BookingEntity(oneId);
        one.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        one.setEndTime(LocalDateTime.of(2000, 1, 1, 1, 2));
        one.setStatus(Status.APPROVED);
        one.setBooker(booker);
        one.setItem(itemModel);
        var two = new BookingEntity(twoId);
        two.setStartTime(LocalDateTime.of(2020, 1, 1, 1, 1));
        two.setEndTime(LocalDateTime.of(2020, 1, 1, 1, 2));
        two.setStatus(Status.APPROVED);
        two.setBooker(booker);
        two.setItem(itemModel);
        var three = new BookingEntity(threeId);
        three.setStartTime(LocalDateTime.of(2040, 1, 1, 1, 1));
        three.setEndTime(LocalDateTime.of(2020, 1, 1, 1, 2));
        three.setStatus(Status.APPROVED);
        three.setBooker(booker);
        three.setItem(itemModel);
        itemModel.setBookingEntityList(List.of(one, two, three));
        when(itemRepository.getById(itemId))
                .thenReturn(itemModel);

        //test
        var check = itemService.getById(userId, itemId);

        //assert
        assertEquals("A", check.getName(), "Некорректное имя после получения.");
        assertEquals(BookingMapper.convertToDto(two).getId(), check.getLastBooking().getId(),
                "Некорректный id последней аренды");
        assertEquals(BookingMapper.convertToDto(three).getId(), check.getNextBooking().getId(),
                "Некорректная id следующей аренды");
    }

    @Test
    public void checkItemUpdated_NotOwn() {
        //data
        Long userId = 1L;
        ItemEntity itemModel = new ItemEntity(
                100500L,
                "B",
                "BBB",
                true,
                100500L,
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
        when(itemRepository.getById(Mockito.anyLong()))
                .thenReturn(itemModel);

        //test
        final NotOwnException e = assertThrows(NotOwnException.class,
                () -> itemService.updated(userId, 100500L, ItemMapper.convertToDto(itemModel)));

        //assert
        assertEquals("Редактировать предмет может только владелец.", e.getMessage());
    }

    @Test
    public void checkItemUpdated_Ok() {
        //data
        Long userId = 1L;
        Long itemId = 100500L;
        var itemDto = new ItemDto();
        itemDto.setName("B");
        itemDto.setDescription("BBB");
        itemDto.setAvailable(true);
        var itemDtoUpd = new ItemDto();
        itemDtoUpd.setName("C");
        itemDtoUpd.setDescription("CCC");
        itemDtoUpd.setAvailable(false);
        when(itemRepository.getById(itemId))
                .thenReturn(ItemMapper.convertToModel(userId, itemDto));
        when(itemRepository.updated(ItemMapper.convertToModel(userId, itemDtoUpd)))
                .thenReturn(ItemMapper.convertToModel(userId, itemDtoUpd));

        //test
        var check = itemService.updated(userId, itemId, itemDtoUpd);

        //assert
        assertEquals("C", check.getName(), "Некорректное имя после обновления.");
        assertEquals("CCC", check.getDescription(), "Некорректное описание после обновления.");
        assertEquals(false, check.getAvailable(), "Некорректный статус доступа.");
    }

    @Test
    public void checkItemSearch_SizeOk() {
        //data
        String text = "text";
        PageRequest pageRequest = PageHelper.createRequest(0, 20);
        Page<ItemEntity> page = new PageImpl<>(List.of(new ItemEntity()));
        when(itemRepository.search(text, pageRequest))
                .thenReturn(page);

        //test
        var check = itemService.search(text, 0, 20);

        //assert
        assertEquals(1, check.size());
    }

    @Test
    public void checkItemAddComment_IdOk() {
        //data
        Long userId = 1L;
        Long ownerId = 2L;
        Long itemId = 100500L;
        Long commentId = 1L;
        var userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("name@name.com");
        var itemModel = new ItemEntity(itemId);
        itemModel.setName("A");
        itemModel.setDescription("AAA");
        itemModel.setAvailable(true);
        itemModel.setOwnerId(ownerId);
        itemModel.setRequestId(null);
        itemModel.setBookingEntityList(new ArrayList<>());
        itemModel.setCommentEntityList(new ArrayList<>());
        var booking = new BookingEntity();
        booking.setItem(itemModel);
        booking.setStartTime(LocalDateTime.of(2023, 1, 1, 1, 1));
        booking.setEndTime(LocalDateTime.of(2023, 1, 1, 1, 2));
        List<BookingEntity> allBookingForThisUser = new ArrayList<>();
        allBookingForThisUser.add(booking);
        var commentDto = new CommentDto();
        commentDto.setText("commentText");
        commentDto.setAuthorName(userDto.getName());
        commentDto.setCreated(LocalDateTime.now());
        var commentModelAfter = new CommentEntity(commentId);
        commentModelAfter.setText(commentDto.getText());
        commentModelAfter.setCreated(commentDto.getCreated());
        commentModelAfter.setUser(UserMapper.convertToModel(userDto));
        commentModelAfter.setItem(itemModel);
        when(userService.getById(userId))
                .thenReturn(userDto);
        when(itemRepository.getById(itemId))
                .thenReturn(itemModel);
        when(bookingRepositoryJpa.findAllByBookerIdNative(userId))
                .thenReturn(allBookingForThisUser);
        when(commentRepositoryJpa.save(any(CommentEntity.class)))
                .thenReturn(commentModelAfter);

        //test
        var check = itemService.addComment(userId, itemId, commentDto);

        //assert
        assertEquals(commentId, check.getId());
    }

    @Test
    public void checkItemAddComment_NotOwn() {
        //data
        Long userId = 1L;
        Long itemId = 100500L;
        Long bookerId = 1L;
        when(userService.getById(userId))
                .thenReturn(new UserDto());
        when(itemRepository.getById(itemId))
                .thenReturn(new ItemEntity());
        when(bookingRepositoryJpa.findAllByBookerIdNative(bookerId))
                .thenReturn(new ArrayList<>());

        //test
        final NotOwnOrCompleteThisBookingException e = assertThrows(NotOwnOrCompleteThisBookingException.class,
                () -> itemService.addComment(userId, itemId, new CommentDto()));

        //assert
        assertEquals("Пользователь не арендовал предмет или не завершил аренду.", e.getMessage());
    }
}
