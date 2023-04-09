package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    /**
     * Добавление пользователя в хранилище.
     * @param user - полная модель.
     * @return полную модель добавленного пользователя.
     */
    User add(User user);

    /**
     * Получение списка всех пользователей из хранилища.
     * @return список полных моделей всех пользователей.
     */
    List<User> getAll();

    /**
     * Получение конкретного пользователя их хранилища.
     * @param id - id конкретного пользователя.
     * @return полную модель конкретного пользователя.
     */
    User getById(int id);

    /**
     * Обновление данных конкретного пользователя в хранилище.
     * @param userId - id конкретного пользователя.
     * @param user полная модель, содержащая данные для обновления.
     * @return полная модель обновленного пользователя.
     */
    User upd(int userId, User user);

    /**
     * Удаление конкретного пользователя из хранилища.
     * @param id - id конкретного пользователя.
     */
    void del(int id);
}
