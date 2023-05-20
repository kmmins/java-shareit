package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.shareit.user.model.UserEntity;

@EnableJpaRepositories
public interface UserRepositoryJpa extends JpaRepository<UserEntity, Long> {
}
