package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnByUserException;
import ru.practicum.shareit.exception.NotOwnOrCompleteThisBookingException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.CommentEntity;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(@Qualifier("itemRepositoryDbImpl") ItemRepository itemRepository,
                           @Qualifier("bookingRepositoryDbImpl") BookingRepository bookingRepository,
                           @Qualifier("commentRepositoryDbImpl") CommentRepository commentRepository,
                           UserService userService) {
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        userService.getById(userId); // проверка на существование пользователя.
        var createdModel = new ItemEntity();
        createdModel.setName(itemDto.getName());
        createdModel.setDescription(itemDto.getDescription());
        createdModel.setAvailable(itemDto.getAvailable());
        createdModel.setOwnerId((userId));
        var afterCreate = itemRepository.add(createdModel);
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
        var userDto = userService.getById(userId);
        var itemEntity = itemRepository.getById(itemId);
        if (itemEntity == null) {
            throw new NotFoundException(String.format("Не найден предмет с id %d.", itemId));
        }
        BookingEntity xBooking = null;
        var allBookingForThisUser = bookingRepository.getAllForUser(userId);
        for (BookingEntity b : allBookingForThisUser) {
            if (Objects.equals(itemId, b.getItem().getId())) {
                xBooking = b;
            }
        }
        if (xBooking != null && xBooking.getEndTime().isBefore(LocalDateTime.now())) {
            var createdComment = new CommentEntity();
            createdComment.setText(commentDto.getText());
            createdComment.setItem(itemEntity);
            createdComment.setUser(UserMapper.convertToModel(userDto));
            createdComment.setCreated(LocalDateTime.now());
            var afterCreate = commentRepository.addComment(createdComment);
            return CommentMapper.convertToDto(afterCreate);
        } else {
            throw new NotOwnOrCompleteThisBookingException("Пользователь не арендовал предмет или не завершил аренду.");
        }
    }
}
