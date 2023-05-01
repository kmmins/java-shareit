package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.repository.BookingRepositoryJpa;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnByUserException;
import ru.practicum.shareit.exception.NotOwnOrCompleteThisBookingException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepositoryJpa;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepositoryJpa bookingRepositoryJpa;
    private final CommentRepositoryJpa commentRepositoryJpa;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(@Qualifier("itemRepositoryDbImpl") ItemRepository itemRepository,
                           CommentRepositoryJpa commentRepositoryJpa,
                           BookingRepositoryJpa bookingRepositoryJpa,
                           UserService userService) {
        this.itemRepository = itemRepository;
        this.commentRepositoryJpa = commentRepositoryJpa;
        this.bookingRepositoryJpa = bookingRepositoryJpa;
        this.userService = userService;
    }

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        userService.getById(userId); // проверка на существование пользователя.
        var createdItem = ItemMapper.convertToModel(userId, itemDto);
        var afterCreate = itemRepository.add(createdItem);
        return ItemMapper.convertToDto(afterCreate);
    }

    @Override
    public List<ItemDto> getAllItemsOwnByUser(Long userId) {
        var allItemThisUserOwn = itemRepository.getAllItemsOwnByUser(userId);
        return ItemMapper.mapToDto(allItemThisUserOwn);
    }

    @Override
    public ItemDto getById(Long userId, Long itemId) {
        var itemGetById = itemRepository.getById(itemId);
        if (itemGetById == null) {
            throw new NotFoundException(String.format("Не найден предмет с id %d.", itemId));
        }
        return ItemMapper.convertToDto(itemGetById, Objects.equals(itemGetById.getOwnerId(), userId));
    }

    @Override
    public ItemDto updated(Long userId, Long itemId, ItemDto itemDto) {
        getById(userId, itemId); // проверка на существование предмета.
        var updatedItem = itemRepository.getById(itemId);
        if (!Objects.equals(updatedItem.getOwnerId(), userId)) {
            throw new NotOwnByUserException("Редактировать предмет может только владелец.");
        }
        if (itemDto.getName() != null) {
            updatedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            updatedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }
        var afterUpdate = itemRepository.updated(updatedItem);
        return ItemMapper.convertToDto(afterUpdate);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        var foundedItems = itemRepository.search(text);
        return ItemMapper.mapToDto(foundedItems);
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        var userEntity = UserMapper.convertToModel(userService.getById(userId));
        var itemEntity = itemRepository.getById(itemId);
        if (itemEntity == null) {
            throw new NotFoundException(String.format("Не найден предмет с id %d.", itemId));
        }
        BookingEntity bookingForComment = null;
        var allBookingForThisUser = bookingRepositoryJpa.findAllByBookerIdOrderByStartTimeDesc(userId);
        for (BookingEntity b : allBookingForThisUser) {
            if (Objects.equals(itemId, b.getItem().getId())) {
                bookingForComment = b;
            }
        }
        if (bookingForComment != null && bookingForComment.getEndTime().isBefore(LocalDateTime.now())) {
            var createdComment = CommentMapper.convertToModel(userEntity, itemEntity, commentDto);
            var afterCreate = commentRepositoryJpa.save(createdComment);
            return CommentMapper.convertToDto(afterCreate);
        } else {
            throw new NotOwnOrCompleteThisBookingException("Пользователь не арендовал предмет или не завершил аренду.");
        }
    }
}
