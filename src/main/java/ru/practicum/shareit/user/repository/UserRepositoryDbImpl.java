package ru.practicum.shareit.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryDbImpl implements UserRepository {

    private final UserRepositoryJpa userRepositoryJpa;

    @Autowired
    public UserRepositoryDbImpl(UserRepositoryJpa userRepositoryJpa) {
        this.userRepositoryJpa = userRepositoryJpa;
    }

    @Override
    public UserEntity add(UserEntity user) {
        return userRepositoryJpa.save(user);
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepositoryJpa.findAll();
    }

    @Override
    public UserEntity getById(Long userId) {
        Optional<UserEntity> result = userRepositoryJpa.findById(userId);
        return result.orElse(null);
    }

    @Override
    public UserEntity updated(UserEntity user) {
        return userRepositoryJpa.save(user);
    }

    @Override
    public void deleted(Long userId) {
        userRepositoryJpa.deleteById(userId);
    }
}

