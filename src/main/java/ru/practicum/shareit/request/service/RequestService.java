package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    /**
     * Добавление реквеста.
     *
     * @param userId     id пользователя отправившего запрос.
     * @param requestDto dto-объект реквеста (поле descr).
     * @return добавленный реквест (dto).
     */
    RequestDto add(Long userId, RequestDto requestDto);

    /**
     * Получение списка своих запросов.
     *
     * @param userId id пользователя отправившего запрос.
     * @return найденный список реквестов (dto).
     */
    List<RequestDto> getAllOwn(Long userId);

    /**
     * Получение списка запросов, созданных другими пользователями.
     *
     * @param userId id пользователя отправившего запрос.
     * @param from   индекс реквеста с которого начинается отображение.
     * @param size   количество реквестов для отображения.
     * @return найденный список реквестов (dto).
     */
    List<RequestDto> getAllFromSize(Long userId, int from, int size);

    /**
     * Получение конкретного реквеста.
     *
     * @param userId    id пользователя отправившего запрос.
     * @param requestId id конкретного реквеста.
     * @return найденный реквест (dto).
     */
    RequestDto getById(Long userId, Long requestId);
}
