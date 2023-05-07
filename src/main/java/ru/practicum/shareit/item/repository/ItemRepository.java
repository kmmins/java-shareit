package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.ItemEntity;

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
    Page<ItemEntity> getAllItemsByOwnerId(Long userId, PageRequest pageRequest);

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
    Page<ItemEntity> search(String text, PageRequest pageRequest);
}
