package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestRequestDto;

import javax.validation.Valid;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody RequestRequestDto requestDto) {
        log.info("Gateway: POST /requests. From user (id): {} add request (dto): {}.", userId, requestDto);
        return requestClient.addRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Gateway: GET /requests. Get all own request for user (id): {}.", userId);
        return requestClient.getAllOwnRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestPageable(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(required = false, defaultValue = "0") int from,
                                                        @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Gateway: GET /requests/all?from={from}&size={size}. Get all request from user (id): {}. " +
                "Page from={} to size={}", userId, from, size);
        return requestClient.getAllRequestPageable(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long requestId) {
        var result = requestClient.getRequestById(userId, requestId);
        log.info("Gateway: GET /requests/{requestId}. Get from user (id): {} request (id): {}. " +
                "Found request (obj): {}.", userId, requestId, result);
        return result;
    }
}
