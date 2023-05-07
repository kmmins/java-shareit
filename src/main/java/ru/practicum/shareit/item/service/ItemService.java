package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    /**
     * Добавление и обработка предмета.
     *
     * @param userId  id владельца предмета.
     * @param itemDto Dto-модель предмета.
     * @return Dto-модель.
     */
    ItemDto add(Long userId, ItemDto itemDto);

    /**
     * Получение и обработка всех предметов конкретного прользователя.
     *
     * @param userId id владельца предмета.
     * @return список Dto-моделей предметов конкретного прользователя.
     */
    List<ItemDto> getAllItemsByOwnerId(Long userId, int from, int size);

    /**
     * Получение и обработка конкретного предмета.
     *
     * @param userId id пользователя создавшего запрос.
     * @param itemId id запрашиваемого предмета.
     * @return Dto-модель конкретного предметов.
     */
    ItemDto getById(Long userId, Long itemId);

    /**
     * Обработка и обновление данных о конкретном предмете.
     *
     * @param userId  id владельца предмета.
     * @param itemId  id конкретного предмета.
     * @param itemDto Dto-модель конкретного предмета, содержашая данные для обновления.
     * @return Dto-модель конкретного предмета после обновления его данных.
     */
    ItemDto updated(Long userId, Long itemId, ItemDto itemDto);

    /**
     * Обработка данных для поиска пердмета, содержашего в названии или описании указанный текст.
     *
     * @param text текст для поиска.
     * @return список Dto-моделей найденных предметов.
     */
    List<ItemDto> search(String text, int from, int size);

    /**
     * Обработка добавления комментария к предмету.
     *
     * @param userId     id автора комментария.
     * @param itemId     id предмета к которому добавлен комментарий.
     * @param commentDto Dto-модель комментария.
     * @return Dto-модель добавленного комментария.
     */
    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
