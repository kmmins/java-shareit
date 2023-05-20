package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserRequestDto userDto) {
        log.info("Gateway: POST /users. Add (user dto): {}.", userDto);
        return userClient.addUser(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Gateway: GET /users.");
        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        log.info("Gateway: GET /users/{id}. Get (user id): {}.", id);
        return userClient.getUserById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,
                                             @RequestBody UserRequestDto userDto) {
        log.info("Gateway: PATCH /users/{id}. Update (user dto): {}.", userDto);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info("Gateway: DELETE /users/{id}. Delete (user id): {}.", id);
        return userClient.deleteUser(id);
    }
}
