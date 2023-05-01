package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.UserEntity;

import java.util.List;

public interface UserRepository {

    /**
     * Добавление пользователя в хранилище.
     *
     * @param user полная модель.
     * @return полную модель добавленного пользователя.
     */
    UserEntity add(UserEntity user);

    /**
     * Получение списка всех пользователей из хранилища.
     *
     * @return список полных моделей всех пользователей.
     */
    List<UserEntity> getAll();

    /**
     * Получение конкретного пользователя их хранилища.
     *
     * @param userId id конкретного пользователя.
     * @return полную модель конкретного пользователя.
     */
    UserEntity getById(Long userId);

    /**
     * Обновление данных конкретного пользователя в хранилище.
     *
     * @param user полная модель, содержащая данные для обновления.
     * @return полная модель обновленного пользователя.
     */
    UserEntity updated(UserEntity user);

    /**
     * Удаление конкретного пользователя из хранилища.
     *
     * @param userId id конкретного пользователя.
     */
    void deleted(Long userId);
}
