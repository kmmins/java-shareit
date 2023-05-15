package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepositoryJpa;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.PageHelper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepositoryJpa bookingRepositoryJpa;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Autowired
    public BookingServiceImpl(BookingRepositoryJpa bookingRepositoryJpa,
                              @Qualifier("itemRepositoryDbImpl") ItemRepository itemRepository,
                              @Qualifier("userServiceImpl") UserService userService) {
        this.bookingRepositoryJpa = bookingRepositoryJpa;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    public BookingDto add(Long userId, BookingDto bookingDto) {
        UserEntity thisUser = UserMapper.convertToModel(userService.getById(userId));
        ItemEntity detectedItem = itemRepository.getById(bookingDto.getItemId());
        if (detectedItem == null) {
            throw new NotFoundException(String.format("Не найден предмет с id %d.", bookingDto.getItemId()));
        }
        if (!detectedItem.getAvailable()) {
            throw new NotAvailableException("Предмет не доступен для аренды.");
        }
        if (Objects.equals(detectedItem.getOwnerId(), userId)) {
            throw new NotFoundException("Нельзя брать в аренду самому у себя.");
        }
        if (!bookingDto.getEnd().isAfter(bookingDto.getStart()) ||
                bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ParameterException("Ошибка с временем аренды.");
        }
        BookingEntity createdBooking = BookingMapper.convertToModel(thisUser, detectedItem, bookingDto);
        createdBooking.setStatus(Status.WAITING);
        BookingEntity afterCreated = bookingRepositoryJpa.save(createdBooking);
        return BookingMapper.convertToDto(afterCreated);
    }

    @Override
    public BookingDto approve(Long userId, Long bookingId, boolean approved) {
        BookingEntity detectedBooking = bookingRepositoryJpa.findById(bookingId).orElse(null);
        if (detectedBooking == null) {
            throw new NotFoundException(String.format("Не найдено бронирование с id %d.", bookingId));
        }
        ItemEntity detectedItem = itemRepository.getById(detectedBooking.getItem().getId());
        if (detectedItem == null) {
            throw new NotFoundException(String.format("Не найден предмет с id %d.", detectedBooking.getItem().getId()));
        }
        if (!userId.equals(detectedItem.getOwnerId())) {
            throw new NotOwnException("Только владелец подтверждает статус брони.");
        }
        if (detectedBooking.getStatus() != Status.WAITING) {
            throw new ParameterException("Подтверждение возможно только находяйщейся в ожидании брони.");
        }
        if (approved) {
            detectedBooking.setStatus(Status.APPROVED);
        } else {
            detectedBooking.setStatus(Status.REJECTED);
        }
        BookingEntity approvedOrNot = bookingRepositoryJpa.save(detectedBooking);
        return BookingMapper.convertToDto(approvedOrNot);
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        BookingEntity booking = bookingRepositoryJpa.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new NotFoundException(String.format("Не найдено бронирование с id %d.", bookingId));
        }
        ItemEntity detectedItem = itemRepository.getById(booking.getItem().getId());
        if (detectedItem == null) {
            throw new NotFoundException(String.format("Не найден предмет с id %d.", booking.getItem().getId()));
        }
        if (!Objects.equals(userId, detectedItem.getOwnerId()) && !Objects.equals(userId, booking.getBooker().getId())) {
            throw new NotOwnItemOrNotOwnBookingException("Данные об аренде доступны только владельцу и арендатору.");
        }
        return BookingMapper.convertToDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingForUser(Long userId, State state, int from, int size) {
        userService.getById(userId);
        PageRequest pageRequest = PageHelper.createRequest(from, size);
        Page<BookingEntity> page = bookingRepositoryJpa.findAllByBookerIdPageNative(userId, pageRequest);
        List<BookingEntity> bookingsForUser = page.getContent();
        return filteringByStateParam(bookingsForUser, state);
    }

    @Override
    public List<BookingDto> getAllBookingForOwnerItems(Long userId, State state, int from, int size) {
        userService.getById(userId);
        PageRequest pageRequest = PageHelper.createRequest(from, size);
        Page<BookingEntity> pageBookings = bookingRepositoryJpa.findAllForItemOwnByUserPage(userId, pageRequest);
        List<BookingEntity> bookingsForOwner = pageBookings.getContent();
        return filteringByStateParam(bookingsForOwner, state);
    }

    private List<BookingDto> filteringByStateParam(List<BookingEntity> bookings, State state) {
        switch (state) {
            case ALL:
                return BookingMapper.mapToDto(bookings);
            case CURRENT:
            case PAST:
            case FUTURE:
            case WAITING:
            case REJECTED:
                return bookings.stream()
                        .filter(b -> isInState(b, state))
                        .map(BookingMapper::convertToDto)
                        .collect(Collectors.toList());
            default:
                throw new ParameterException("Некорректный параметр State: " + state + ".");
        }
    }

    private static boolean isInState(BookingEntity booking, State state) {
        switch (state) {
            case CURRENT:
                return booking.getStartTime().isBefore(LocalDateTime.now()) &&
                        booking.getEndTime().isAfter(LocalDateTime.now());
            case PAST:
                return booking.getEndTime().isBefore(LocalDateTime.now());
            case FUTURE:
                return booking.getStartTime().isAfter(LocalDateTime.now());
            case WAITING:
                return booking.getStatus().equals(Status.WAITING);
            case REJECTED:
                return booking.getStatus().equals(Status.REJECTED) || booking.getStatus().equals(Status.CANCELED);
        }
        return false;
    }
}
