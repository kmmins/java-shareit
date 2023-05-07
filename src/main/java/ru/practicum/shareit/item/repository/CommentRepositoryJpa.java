package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.shareit.item.model.CommentEntity;

@EnableJpaRepositories
public interface CommentRepositoryJpa extends JpaRepository<CommentEntity, Long> {

}
