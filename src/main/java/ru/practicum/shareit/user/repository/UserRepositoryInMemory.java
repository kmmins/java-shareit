package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepositoryInMemory implements UserRepository {
    private int countUsers = 0;
    private final HashMap<Integer, User> userMemoryUserBase = new HashMap<>();

    @Override
    public User add(User user) {
        countUsers++;
        var createdUser = new User(
                countUsers,
                user.getName(),
                user.getEmail()
        );
        userMemoryUserBase.put(countUsers, createdUser);
        return createdUser;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMemoryUserBase.values());
    }

    @Override
    public User getById(int id) {
        return userMemoryUserBase.get(id);
    }

    @Override
    public User upd(int id, User user) {
        var updatedUser = userMemoryUserBase.get(id);
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());

        userMemoryUserBase.put(id, updatedUser);
        return updatedUser;
    }

    @Override
    public void del(int id) {
        userMemoryUserBase.remove(id);
    }
}
