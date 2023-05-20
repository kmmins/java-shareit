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
        log.info("Gateway: POST /requests. Add (request dto): {}.", requestDto);
        return requestClient.addRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Gateway: GET /requests.");
        return requestClient.getAllOwnRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestPageable(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(required = false, defaultValue = "0") int from,
                                                        @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Gateway: GET /requests/all?from={from}&size={size}. Page from={} to size={}", from, size);
        return requestClient.getAllRequestPageable(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Gateway: GET /requests/{requestId}.");
        return requestClient.getRequestById(userId, requestId);
    }
}
