package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.CommentEntity;

@Repository
public class CommentRepositoryDbImpl implements CommentRepository {

    private final CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    public CommentRepositoryDbImpl(CommentRepositoryJpa commentRepositoryJpa) {
        this.commentRepositoryJpa = commentRepositoryJpa;
    }

    @Override
    public CommentEntity addComment(CommentEntity comment) {
        return commentRepositoryJpa.save(comment);
    }
}
