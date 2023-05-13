package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepositoryJpa;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepositoryDbImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.PageHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplUnitTests {

    @InjectMocks
    BookingServiceImpl bookingService;
    @Mock
    ItemRepositoryDbImpl itemRepository;
    @Mock
    BookingRepositoryJpa bookingRepositoryJpa;
    @Mock
    UserService userService;

    @Test
    public void checkBookingAdd_ItemNotFound() {
        //data
        Long userId = 1L;
        Long wrongItemId = 666L;
        Long bookingId = 1L;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setBooker(userDto);
        bookingDto.setItemId(wrongItemId);
        when(itemRepository.getById(wrongItemId))
                .thenReturn(null);
        when(userService.getById(userId))
                .thenReturn(userDto);

        //test
        final NotFoundException e = assertThrows(NotFoundException.class,
                () -> bookingService.add(userId, bookingDto));

        //assert
        assertEquals(String.format("Не найден предмет с id %d.", wrongItemId), e.getMessage());
    }

    @Test
    public void checkBookingAdd_ItemNotAvailable() {
        //data
        Long userId = 1L;
        Long itemId = 100500L;
        Long bookingId = 1L;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var itemModel = new ItemEntity(itemId);
        itemModel.setAvailable(false);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setBooker(userDto);
        bookingDto.setItemId(itemId);
        when(itemRepository.getById(itemId))
                .thenReturn(itemModel);
        when(userService.getById(userId))
                .thenReturn(userDto);

        //test
        final NotAvailableException e = assertThrows(NotAvailableException.class,
                () -> bookingService.add(userId, bookingDto));

        //assert
        assertEquals("Предмет не доступен для аренды.", e.getMessage());
    }

    @Test
    public void checkBookingAdd_BookingFromItemOwner() {
        //data
        Long userId = 1L;
        Long itemId = 100500L;
        Long bookingId = 1L;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var itemModel = new ItemEntity(itemId);
        itemModel.setName("A");
        itemModel.setDescription("AAA");
        itemModel.setAvailable(true);
        itemModel.setOwnerId(userId);
        var bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setBooker(userDto);
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.of(2023, 1, 1, 1, 1));
        bookingDto.setEnd(LocalDateTime.of(2023, 1, 1, 1, 2));
        when(itemRepository.getById(itemId))
                .thenReturn(itemModel);
        when(userService.getById(userId))
                .thenReturn(userDto);

        //test
        final NotFoundException e = assertThrows(NotFoundException.class,
                () -> bookingService.add(userId, bookingDto));

        //assert
        assertEquals("Нельзя брать в аренду самому у себя.", e.getMessage());
    }

    @Test
    public void checkBookingAdd_WrongTime() {
        //data
        Long userId = 1L;
        Long itemId = 100500L;
        Long bookingId = 1L;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var itemModel = new ItemEntity(itemId);
        itemModel.setAvailable(true);
        var bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setBooker(userDto);
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.of(2023, 1, 1, 1, 2));
        bookingDto.setEnd(LocalDateTime.of(2023, 1, 1, 1, 1));
        var bookingDto2 = new BookingDto();
        bookingDto2.setId(bookingId);
        bookingDto2.setBooker(userDto);
        bookingDto2.setItemId(itemId);
        bookingDto2.setStart(LocalDateTime.of(2022, 1, 1, 1, 1));
        bookingDto2.setEnd(LocalDateTime.of(2024, 1, 1, 1, 1));
        when(itemRepository.getById(itemId))
                .thenReturn(itemModel);
        when(userService.getById(userId))
                .thenReturn(userDto);

        //test
        final ParameterException e1 = assertThrows(ParameterException.class,
                () -> bookingService.add(userId, bookingDto));
        final ParameterException e2 = assertThrows(ParameterException.class,
                () -> bookingService.add(userId, bookingDto2));

        //assert
        assertEquals("Ошибка с временем аренды.", e1.getMessage());
        assertEquals("Ошибка с временем аренды.", e2.getMessage());
    }

    @Test
    public void checkBookingAdd_IdOk() {
        //data
        Long userId = 1L;
        Long itemId = 100500L;
        Long ownerId = 2L;
        Long bookingId = 1L;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var itemModel = new ItemEntity(itemId);
        itemModel.setName("A");
        itemModel.setDescription("AAA");
        itemModel.setAvailable(true);
        itemModel.setOwnerId(ownerId);
        itemModel.setRequestId(null);
        var bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setBooker(userDto);
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.of(2222, 1, 1, 1, 1));
        bookingDto.setEnd(LocalDateTime.of(2222, 1, 1, 1, 2));
        var created = BookingMapper.convertToModel(UserMapper.convertToModel(userDto), itemModel, bookingDto);
        created.setStatus(Status.WAITING);
        var afterCreated = new BookingEntity(bookingId);
        afterCreated.setStartTime(LocalDateTime.of(2222, 1, 1, 1, 1));
        afterCreated.setEndTime(LocalDateTime.of(2222, 1, 1, 1, 2));
        afterCreated.setStatus(Status.WAITING);
        afterCreated.setBooker(UserMapper.convertToModel(userDto));
        afterCreated.setItem(itemModel);
        when(userService.getById(userId))
                .thenReturn(userDto);
        when(itemRepository.getById(itemId))
                .thenReturn(itemModel);
        when(bookingRepositoryJpa.save(created))
                .thenReturn(afterCreated);

        //test
        var check = bookingService.add(userId, bookingDto);

        //assert
        assertEquals(bookingId, check.getId(), "Некорректный id бронирования." + check.getId());
    }

    @Test
    public void checkBookingApprove_ThrowExceptionsAndOk() {
        //data
        Long userId = 1L;
        Long wrongUserId = 666L;
        Long bookingId = 1L;
        Long wrongBookingId = 666L;
        Long ownerId = 1L;
        Long itemId = 100500L;
        boolean approvedTrue = true;
        boolean approvedFalse = false;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var itemModel = new ItemEntity(itemId);
        itemModel.setName("A");
        itemModel.setDescription("AAA");
        itemModel.setAvailable(true);
        itemModel.setOwnerId(ownerId);
        itemModel.setRequestId(null);
        itemModel.setBookingEntityList(new ArrayList<>());
        itemModel.setCommentEntityList(new ArrayList<>());
        var bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setBooker(userDto);
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.of(2222, 6, 16, 12, 0));
        bookingDto.setEnd(LocalDateTime.of(2222, 6, 18, 12, 0));
        var created = BookingMapper.convertToModel(UserMapper.convertToModel(userDto), itemModel, bookingDto);
        created.setStatus(Status.WAITING);
        var wrongDto = new BookingDto();
        wrongDto.setId(wrongBookingId);
        wrongDto.setBooker(userDto);
        wrongDto.setItemId(itemId);
        wrongDto.setStart(LocalDateTime.of(2222, 6, 16, 12, 0));
        wrongDto.setEnd(LocalDateTime.of(2222, 6, 18, 12, 0));
        var wrong = BookingMapper.convertToModel(UserMapper.convertToModel(userDto), itemModel, wrongDto);
        wrong.setStatus(Status.CANCELED);
        var afterApprove = new BookingEntity(bookingId);
        afterApprove.setStartTime(LocalDateTime.of(2222, 6, 16, 12, 0));
        afterApprove.setEndTime(LocalDateTime.of(2222, 6, 18, 12, 0));
        afterApprove.setStatus(Status.REJECTED);
        afterApprove.setBooker(UserMapper.convertToModel(userDto));
        afterApprove.setItem(itemModel);
        when(bookingRepositoryJpa.findById(bookingId))
                .thenReturn(Optional.of(created));
        when(bookingRepositoryJpa.findById(wrongBookingId))
                .thenReturn(Optional.of(wrong));
        when(itemRepository.getById(itemId))
                .thenReturn(itemModel);
        when(bookingRepositoryJpa.save(created))
                .thenReturn(afterApprove);

        //test
        final NotOwnException e1 = assertThrows(NotOwnException.class,
                () -> bookingService.approve(wrongUserId, bookingId, approvedTrue));
        final ParameterException e2 = assertThrows(ParameterException.class,
                () -> bookingService.approve(userId, wrongBookingId, approvedTrue));
        var check = bookingService.approve(userId, bookingId, approvedFalse);

        //assert
        assertEquals("Только владелец подтверждает статус брони.", e1.getMessage());
        assertEquals("Подтверждение возможно только находяйщейся в ожидании брони.", e2.getMessage());
        assertEquals(Status.REJECTED, check.getStatus(), "Некорректный статус после подтверждения.");
    }

    @Test
    public void checkBookingGetById_IdOkAndWrongUser() {
        //data
        Long userId = 1L;
        Long wrongUserId = 666L;
        Long bookingId = 1L;
        Long ownerId = 1L;
        Long itemId = 100500L;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var itemModel = new ItemEntity(itemId);
        itemModel.setName("A");
        itemModel.setDescription("AAA");
        itemModel.setAvailable(true);
        itemModel.setOwnerId(ownerId);
        itemModel.setRequestId(null);
        itemModel.setBookingEntityList(new ArrayList<>());
        itemModel.setCommentEntityList(new ArrayList<>());
        var bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setBooker(userDto);
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.of(2000, 1, 1, 1, 1));
        bookingDto.setEnd(LocalDateTime.of(2000, 1, 1, 1, 2));
        var created = BookingMapper.convertToModel(UserMapper.convertToModel(userDto), itemModel, bookingDto);
        created.setStatus(Status.APPROVED);
        var afterGetById = new BookingEntity(bookingId);
        afterGetById.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        afterGetById.setEndTime(LocalDateTime.of(2000, 1, 1, 1, 2));
        afterGetById.setStatus(Status.APPROVED);
        afterGetById.setBooker(UserMapper.convertToModel(userDto));
        afterGetById.setItem(itemModel);
        when(itemRepository.getById(itemId))
                .thenReturn(itemModel);
        when(bookingRepositoryJpa.findById(bookingId))
                .thenReturn(Optional.of(afterGetById));

        //test
        var check = bookingService.getById(userId, bookingId);
        final NotOwnItemOrNotOwnBookingException e1 = assertThrows(NotOwnItemOrNotOwnBookingException.class,
                () -> bookingService.getById(wrongUserId, bookingId));

        //assert
        assertEquals(bookingId, check.getId(), "Некорректный id бронирования.");
        assertEquals("Данные об аренде доступны только владельцу и арендатору.", e1.getMessage());
    }

    @Test
    public void checkBookingGetAllBookingForUser_StateOk() {
        //data
        Long oneId = 1L;
        Long twoId = 2L;
        Long threeId = 3L;
        Long fourId = 4L;
        Long fiveId = 5L;
        Long userId = 1L;
        Long bookerId = 2L;
        Long ownerId = 1L;
        Long itemId = 100500L;
        State past = State.PAST;
        State future = State.FUTURE;
        State rejected = State.REJECTED;
        State current = State.CURRENT;
        State waiting = State.WAITING;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var booker = new UserEntity(bookerId, "booker", "booker@booker.com");
        var itemModel = new ItemEntity(itemId);
        itemModel.setName("A");
        itemModel.setDescription("AAA");
        itemModel.setAvailable(true);
        itemModel.setOwnerId(ownerId);
        itemModel.setRequestId(null);
        var one = new BookingEntity(oneId);
        one.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        one.setEndTime(LocalDateTime.of(2000, 1, 1, 1, 2));
        one.setStatus(Status.APPROVED);
        one.setBooker(booker);
        one.setItem(itemModel);
        var two = new BookingEntity(twoId);
        two.setStartTime(LocalDateTime.of(2111, 1, 1, 1, 1));
        two.setEndTime(LocalDateTime.of(2111, 1, 1, 1, 2));
        two.setStatus(Status.APPROVED);
        two.setBooker(booker);
        two.setItem(itemModel);
        var three = new BookingEntity(threeId);
        three.setStartTime(LocalDateTime.of(2222, 1, 1, 1, 1));
        three.setEndTime(LocalDateTime.of(2222, 1, 1, 1, 2));
        three.setStatus(Status.APPROVED);
        three.setBooker(booker);
        three.setItem(itemModel);
        var four = new BookingEntity(fourId);
        four.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        four.setEndTime(LocalDateTime.of(3000, 1, 1, 1, 2));
        four.setStatus(Status.CANCELED);
        four.setBooker(booker);
        four.setItem(itemModel);
        var five = new BookingEntity(fiveId);
        five.setStartTime(LocalDateTime.of(2022, 1, 1, 1, 1));
        five.setEndTime(LocalDateTime.of(2222, 1, 1, 1, 2));
        five.setStatus(Status.APPROVED);
        five.setBooker(booker);
        five.setItem(itemModel);
        List<BookingEntity> list = List.of(one, two, three, four, five);
        Page<BookingEntity> page = new PageImpl<>(list);
        PageRequest pageRequest = PageHelper.createRequest(0, 20);
        when(userService.getById(userId))
                .thenReturn(userDto);
        when(bookingRepositoryJpa.findAllByBookerIdPageNative(userId, pageRequest))
                .thenReturn(page);

        //test
        var check1 = bookingService.getAllBookingForUser(userId, past, 0, 20);
        var check2 = bookingService.getAllBookingForUser(userId, future, 0, 20);
        var check3 = bookingService.getAllBookingForUser(userId, rejected, 0, 20);
        var check4 = bookingService.getAllBookingForUser(userId, current, 0, 20);
        var check5 = bookingService.getAllBookingForUser(userId, waiting, 0, 20);

        //assert
        assertEquals(1, check1.size(), "Некорректное число прошлых бронирований.");
        assertEquals(2, check2.size(), "Некорректное число будующих бронирований.");
        assertEquals(Status.CANCELED, check3.get(0).getStatus(), "Некорректный статус бронирования.");
        assertTrue(check4.get(0).getStart().isBefore(LocalDateTime.now()), "Ошибка с временем начала.");
        assertTrue(check4.get(0).getEnd().isAfter(LocalDateTime.now()), "Ошибка с временем окончания.");
        assertEquals(0, check5.size(), "Некорректное число бронирований на подтверждении.");
    }

    @Test
    public void checkBookingGetAllBookingForOwnerItems_AllOkAndWrongPageParam() {
        //data
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemIdA = 100500L;
        Long itemIdB = 500100L;
        Long oneId = 1L;
        Long twoId = 2L;
        State All = State.ALL;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var booker = new UserEntity(bookerId, "booker", "booker@booker.com");
        var itemModelA = new ItemEntity(itemIdA);
        itemModelA.setName("A");
        itemModelA.setDescription("AAA");
        itemModelA.setAvailable(true);
        itemModelA.setOwnerId(userId);
        itemModelA.setRequestId(null);
        var itemModelB = new ItemEntity(itemIdB);
        itemModelB.setName("B");
        itemModelB.setDescription("BBB");
        itemModelB.setAvailable(true);
        itemModelB.setOwnerId(userId);
        itemModelB.setRequestId(null);
        var one = new BookingEntity(oneId);
        one.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        one.setEndTime(LocalDateTime.of(2000, 1, 1, 1, 2));
        one.setStatus(Status.APPROVED);
        one.setBooker(booker);
        one.setItem(itemModelA);
        var two = new BookingEntity(twoId);
        two.setStartTime(LocalDateTime.of(2111, 1, 1, 1, 1));
        two.setEndTime(LocalDateTime.of(2111, 1, 1, 1, 2));
        two.setStatus(Status.APPROVED);
        two.setBooker(booker);
        two.setItem(itemModelB);
        List<BookingEntity> list = List.of(one, two);
        Page<BookingEntity> page = new PageImpl<>(list);
        PageRequest pageRequest = PageHelper.createRequest(0, 20);
        when(userService.getById(userId))
                .thenReturn(userDto);
        when(bookingRepositoryJpa.findAllForItemOwnByUserPage(userId, pageRequest))
                .thenReturn(page);

        //test
        var check1 = bookingService.getAllBookingForOwnerItems(userId, All, 0, 20);
        final ParameterException e1 = assertThrows(ParameterException.class,
                () -> bookingService.getAllBookingForOwnerItems(userId, All, -1, 20));
        final ParameterException e2 = assertThrows(ParameterException.class,
                () -> bookingService.getAllBookingForOwnerItems(userId, All, 0, -1));

        //assert
        assertEquals(2, check1.size(), "Некорректное число всех бронирований.");
        assertEquals("Ошибка с параметром from.", e1.getMessage());
        assertEquals("Ошибка с параметром size.", e2.getMessage());
    }
}
