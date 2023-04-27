package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.ItemEntity;

import java.util.List;

public interface ItemRepository {

    /**
     * Добавление предмета в хранилище.
     *
     * @param item на входе полная модель.
     * @return полную модель добавленного предмета.
     */
    ItemEntity add(ItemEntity item);

    /**
     * Получить все предметы конкретного пользователя.
     *
     * @param userId id пользователя.
     * @return список предметов конкретного пользователя.
     */
    List<ItemEntity> getAllItemsOwnByUser(Long userId);

    /**
     * Получить конкретный предмет из хранилища.
     *
     * @param id id предмета.
     * @return полную модель.
     */
    ItemEntity getById(Long id);

    /**
     * Обновить свойства предмета в хранилище.
     *
     * @param item на входе модель предмета.
     * @return полную модель обновленного предмета.
     */
    ItemEntity updated(ItemEntity item);

    /**
     * Поиск в хранилище предмета, содержашего в названии или описании указанный текст.
     *
     * @param text текст для поиска.
     * @return список полных моделей предметов.
     */
    List<ItemEntity> search(String text);
}
