package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    /**
     * Добавление и обработка пользователя.
     * @param userDto - Dto-модель пользователя.
     * @return Dto-модель пользователя.
     */
    UserDto add(UserDto userDto);

    /**
     * Получение и обработка всех пользователей.
     * @return список Dto-моделей пользователей.
     */
    List<UserDto> getAll();

    /**
     * Получение и обработка конкретного пользователя.
     * @param userId - id конкретного пользователя.
     * @return Dto-модель конкретного пользователя.
     */
    UserDto getById(Long userId);

    /**
     * Обработка и обновление данных о конктретном пользователе.
     * @param userDto - Dto-модель пользователя, содержащая данные для обновления.
     * @return Dto-модель обновленного пользователя.
     */
    UserDto updated(Long userId, UserDto userDto);

    /**
     * Проверка существования и удаление конкретного пользователя
     * @param userId - id конкретного пользователя.
     */
    void deleted(Long userId);
}
