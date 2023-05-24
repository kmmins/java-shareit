package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

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
    public ItemController(@Qualifier("itemServiceImpl") ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody ItemDto itemDto) {
        var addedItemDto = itemService.add(userId, itemDto);
        log.info("Обработка запроса POST /items. Предмет добавлен: {}.", addedItemDto);
        return addedItemDto;
    }

    @GetMapping
    public List<ItemDto> getAllItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam(required = false, defaultValue = "0") int from,
                                    @RequestParam(required = false, defaultValue = "10") int size) {
        var allItemsDto = itemService.getAllItemsByOwnerId(userId, from, size);
        log.info("Обработка запроса GET /items?from={from}&size={size}. Получены все предметы владельца: {}.", allItemsDto);
        return allItemsDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getByIdItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long itemId) {
        var itemDtoByIdDto = itemService.getById(userId, itemId);
        log.info("Обработка запроса GET /items/{itemId}. Получены данные предмета: {}.", itemDtoByIdDto);
        return itemDtoByIdDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        var updatedItemDto = itemService.updated(userId, itemId, itemDto);
        log.info("Обработка запроса PATCH /items/{itemId}. Предмет обновлен: {}.", updatedItemDto);
        return updatedItemDto;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text,
                                    @RequestParam(required = false, defaultValue = "0") int from,
                                    @RequestParam(required = false, defaultValue = "10") int size) {
        var foundItemsDto = itemService.search(text, from, size);
        log.info("Обработка запроса GET /items/search?text={text}&from={from}&size={size}. Предмет найден: {}.", foundItemsDto);
        return foundItemsDto;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto) {
        var addedCommentDto = itemService.addComment(userId, itemId, commentDto);
        log.info("Обработка запроса POST /items/{itemId}/comment. Комментарий добавлен: {}.", addedCommentDto);
        return addedCommentDto;
    }
}
