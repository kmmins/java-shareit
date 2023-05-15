package ru.practicum.shareit.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public RequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody RequestDto requestDto) {
        var addedRequestDto = requestService.add(userId, requestDto);
        log.info("Обработка запроса POST /requests. Запрос добавлен: {}.", addedRequestDto);
        return addedRequestDto;
    }

    @GetMapping
    public List<RequestDto> getAllOwnRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        var allOwnRequest = requestService.getAllOwn(userId);
        log.info("Обработка запроса GET /requests. Собственные реквесты получены: {}.", allOwnRequest);
        return allOwnRequest;
    }

    @GetMapping("/all")
    public List<RequestDto> getAllRequestPageable(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(required = false, defaultValue = "0") int from,
                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        var allRequestPageable = requestService.getAllFromSize(userId, from, size);
        log.info("Обработка запроса GET /requests/all?from={from}&size={size}. " +
                "Запросы от пользователя получены: {}.", allRequestPageable);
        return allRequestPageable;
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long requestId) {
        var requestDtoById = requestService.getById(userId, requestId);
        log.info("Обработка запроса GET /requests/{requestId}. Запрос получеы: {}.", requestDtoById);
        return requestDtoById;
    }
}
