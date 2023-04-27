package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.CommentEntity;

public interface CommentRepository {

    /**
     * Добавление отзыва к предмету пользователем, который брал этот предмет в аренду.
     *
     * @param comment комментарий (внутреннее представление).
     * @return добавленный в хранилище комментарий (внутреннее представление).
     */
    CommentEntity addComment(CommentEntity comment);
}
