package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(@Qualifier("itemServiceInMemory") ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @Valid @RequestBody ItemDto itemDto) {
        var addedItemDto = itemService.add(userId, itemDto);
        log.info("Обработка запроса POST /items. Предмет добавлен: {}.", addedItemDto);
        return addedItemDto;
    }

    @GetMapping
    public List<ItemDto> getAllItem(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        var allItemsDto = itemService.getAll(userId);
        log.info("Обработка запроса GET /items. Получены все предметы владельца: {}.", allItemsDto);
        return allItemsDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getByIdItem(@PathVariable Integer itemId) {
        var itemDtoByIdDto = itemService.getById(itemId);
        log.info("Обработка запроса GET /items/{itemId}. Получены данные предмета: {}.", itemDtoByIdDto);
        return itemDtoByIdDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer itemId,
                           @RequestBody ItemDto itemDto) {
        var updatedItemDto = itemService.upd(userId, itemId, itemDto);
        log.info("Обработка запроса PATCH /items/{itemId}. Предмет обновлен: {}.", updatedItemDto);
        return updatedItemDto;
    }

    @GetMapping("/search")
    public List<ItemDto> findItem(@RequestParam String text) {
        var foundItemsDto = itemService.getFound(text);
        log.info("Обработка запроса GET /items/search?text={text}. Предмет найден: {}.", foundItemsDto);
        return foundItemsDto;
    }
}
