package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    /**
     * Добавление и обработка предмета.
     * @param userId - id владельца предмета.
     * @param itemDto - Dto-модель предмета.
     * @return Dto-модель.
     */
    ItemDto add(int userId, ItemDto itemDto);

    /**
     * Получение и обрадока всех предметов конкретного прользователя.
     * @param userId - id владельца предмета.
     * @return список Dto-моделей предметов конкретного прользователя.
     */
    List<ItemDto> getAll(int userId);

    /**
     * Получение и обработка конкретного предмета.
     * @param itemId - id конкретного предмета.
     * @return Dto-модель конкретного предмета.
     */
    ItemDto getById(int itemId);

    /**
     * Обработка и обновление данных о конкретном предмете.
     * @param userId - id владельца предмета.
     * @param itemId - id конкретного предмета.
     * @param itemDto - Dto-модель конкретного предмета, содержашая данные для обновления.
     * @return Dto-модель конкретного предмета после обновления его данных.
     */
    ItemDto upd(int userId, int itemId, ItemDto itemDto);

    /**
     * Обработка данных для поиска пердмета, содержашего в названии или описании указанный текст.
     * @param text - текст для поиска.
     * @return список Dto-моделей найденных предметов.
     */
    List<ItemDto> getFound(String text);
}
