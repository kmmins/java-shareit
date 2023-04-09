package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    /**
     * Добавление предмета в хранилище.
     * @param item на входе полная модель.
     * @return полную модель добваленного предмета.
     */
    Item add(Item item);

    /**
     * Получить все предметы конкретного пользователя.
     * @param userId - id пользователя.
     * @return список предметов конкретного пользователя.
     */
    List<Item> getAll(int userId);

    /**
     * Получить конкретный предмент из хранилища.
     * @param id - id предмета.
     * @return полную модель.
     */
    Item getById(int id);

    /**
     * Обновить свойства предмета в хранилище.
     * @param itemId - id предмета.
     * @param item на входе модель предмета.
     * @return полную модель обновленного предмета.
     */
    Item upd(int itemId, Item item);

    /**
     * Поиск в хранилище предмета, содержашего в названии или описании указанный текст.
     * @param text - текст для поиска.
     * @return список полных моделей предметов.
     */
    List<Item> getFound(String text);
}
