package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotOwnedByUserException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemServiceInMemory implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Autowired
    public ItemServiceInMemory(@Qualifier("itemRepositoryInMemory") ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    public ItemDto add(int userId, ItemDto itemDto) {
        userService.getById(userId); // проверка на существование.
        var createdItem = ItemMapper.convertToModel(userId, itemDto);
        var afterCreate = itemRepository.add(createdItem);
        return ItemMapper.convertToDto(afterCreate);
    }

    @Override
    public List<ItemDto> getAll(int userId) {
        return itemRepository.getAll(userId).stream()
                .map(ItemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(int itemId) {
        // userId можно использовать для подсчета кол-ва просмотров?
        var itemGetById = itemRepository.getById(itemId);
        if (itemGetById == null) {
            throw new ItemNotFoundException(String.format("Не найден предмет с id %d.", itemId));
        }
        return ItemMapper.convertToDto(itemGetById);
    }

    @Override
    public ItemDto upd(int userId, int itemId, ItemDto itemDto) {
        getById(itemId); // проверка на существование.
        var updatedItem = itemRepository.getById(itemId);
        if (!Objects.equals(updatedItem.getOwnerId(), userId)) {
            throw new ItemNotOwnedByUserException("Редактировать предмет может только владелец.");
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

        var afterUpdate = itemRepository.upd(itemId, updatedItem);
        return ItemMapper.convertToDto(afterUpdate);
    }

    @Override
    public List<ItemDto> getFound(String text) {
        var lowerCaseText = text.toLowerCase();
        var foundedItems = itemRepository.getFound(lowerCaseText);
        return foundedItems.stream()
                .map(ItemMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
