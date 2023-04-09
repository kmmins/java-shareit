package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("userServiceInMemory") UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        var addedUserDto = userService.add(userDto);
        log.info("Обработка запроса POST /users. Пользователь добавлен: {}.", addedUserDto);
        return addedUserDto;
    }

    @GetMapping
    public List<UserDto> getAllUser() {
        var allUsersDto = userService.getAll();
        var size = allUsersDto.size();
        log.info("Обработка запроса GET /users. Всего пользователей: {}.", size);
        return allUsersDto;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Integer id) {
        var userByIdDto = userService.getById(id);
        log.info("Обработка запроса GET /users/{id}. Получены данные пользователя: {}.", userByIdDto);
        return userByIdDto;
    }

    @PatchMapping("/{id}")
    public UserDto updUser(@PathVariable Integer id,
                           @RequestBody UserDto userDto) {
        var updatedUserDto = userService.upd(id, userDto);
        log.info("Обработка запроса PATCH /users/{id}. Пользователь обновлен: {}.", updatedUserDto);
        return updatedUserDto;
    }

    @DeleteMapping("/{id}")
    public void delUser(@PathVariable Integer id) {
        userService.del(id);
        log.info("Обработка запроса DELETE /users/{id}. Пользователь c id: {} удален.", id);
    }
}
