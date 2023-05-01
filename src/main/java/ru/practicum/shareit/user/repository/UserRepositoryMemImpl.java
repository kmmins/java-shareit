package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepositoryMemImpl implements UserRepository {

    private Long countUsers = 0L;
    private final HashMap<Long, UserEntity> userMemoryUserBase = new HashMap<>();

    @Override
    public UserEntity add(UserEntity user) {
        countUsers++;
        var createdUser = new UserEntity(
                countUsers,
                user.getName(),
                user.getEmail()
        );
        userMemoryUserBase.put(countUsers, createdUser);
        return createdUser;
    }

    @Override
    public List<UserEntity> getAll() {
        return new ArrayList<>(userMemoryUserBase.values());
    }

    @Override
    public UserEntity getById(Long userId) {
        return userMemoryUserBase.get(userId);
    }

    @Override
    public UserEntity updated(UserEntity user) {
        var updatedUser = userMemoryUserBase.get(user.getId());
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        userMemoryUserBase.put(user.getId(), updatedUser);
        return updatedUser;
    }

    @Override
    public void deleted(Long userId) {
        userMemoryUserBase.remove(userId);
    }
}
