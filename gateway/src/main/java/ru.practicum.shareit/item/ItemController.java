package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody ItemRequestDto itemDto) {
        log.info("Gateway: POST /items. Add from user (id) {} item (dto): {}.", userId, itemDto);
        return itemClient.addItem(userId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Gateway: GET /items?from={from}&size={size}. Get all for user (id): {}. " +
                "Page from={}, size={}", userId, from, size);
        return itemClient.getAllItem(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getByIdItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long itemId) {
        var result = itemClient.getByIdItem(userId, itemId);
        log.info("Gateway: GET /items/{itemId}. Get item (id): {}, userId={}. " +
                "Found item (obj): {}.", itemId, userId, result);
        return result;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody ItemRequestDto itemDto) {
        log.info("Gateway: PATCH /items/{itemId}. Update from user (id): {} item (id): {}. " +
                "Item (dto) to update: {}.", userId, itemId, itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam String text,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Gateway: GET /items/search?text={text}&from={from}&size={size}. " +
                "Search text={}, Page from={} to size={}.", text, from, size);
        return itemClient.searchItem(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentRequestDto commentDto) {
        log.info("Gateway: POST /items/{itemId}/comment. Add comment from user (id): {} to item (id): {}. " +
                "Comment (dto): {}.", userId, itemId, commentDto);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
