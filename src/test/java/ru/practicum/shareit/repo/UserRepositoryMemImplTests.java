package ru.practicum.shareit.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepositoryMemImpl;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserRepositoryMemImplTests {

    @Autowired
    private UserRepositoryMemImpl userRepository;

    @BeforeEach
    public void addData() {
        var user0 = new UserEntity();
        user0.setName("name");
        user0.setEmail("name@rock.rs");
        userRepository.add(user0);
    }

    @Test
    public void checkUserAddMem() {
        var user1 = new UserEntity();
        user1.setName("aaa");
        user1.setEmail("aaa@rock.rs");
        var before = userRepository.getAll();

        var result = userRepository.add(user1);

        assertEquals(before.size() + 1, result.getId(), "Проверка size - ошибка.");
        assertEquals("aaa", result.getName(), "Проверка name - ошибка.");
        assertEquals("aaa@rock.rs", result.getEmail(), "Проверка email - ошибка.");
    }

    @Test
    public void checkUserGetAllMem() {
        var user1 = new UserEntity();
        user1.setName("bbb");
        user1.setEmail("bbb@rock.rs");
        var user2 = new UserEntity();
        user2.setName("ccc");
        user2.setEmail("ccc@rock.rs");
        var user3 = new UserEntity();
        user3.setName("ddd");
        user3.setEmail("ddd@rock.rs");
        var before = userRepository.getAll();
        userRepository.add(user1);
        userRepository.add(user2);
        userRepository.add(user3);


        var result = userRepository.getAll();

        assertEquals(before.size() + 3, result.size(), "Проверка size - ошибка.");
    }

    @Test
    public void checkUserGetByIdMem() {
        var before = userRepository.getAll().stream()
                .max((Comparator.comparing(UserEntity::getId)))
                .orElse(null);
        long expectedId;
        if (before != null) {
            expectedId = before.getId();
        } else {
            expectedId = 1L;
        }

        var result = userRepository.getById(expectedId);

        assertEquals(expectedId, result.getId(), "Проверка id - ошибка.");
    }

    @Test
    public void checkUserUpdatedMem() {
        var before = userRepository.getAll();
        var toUpd = before.get(0);
        toUpd.setName("aaaUpd");
        toUpd.setEmail("aaaUpd@rock.rs");

        var result = userRepository.updated(toUpd);

        assertEquals("aaaUpd", result.getName(), "Проверка name - ошибка.");
        assertEquals("aaaUpd@rock.rs", result.getEmail(), "Проверка email - ошибка.");
    }

    @Test
    public void checkUserDeletedMem() {
        var before = userRepository.getAll();

        userRepository.deleted(before.get(0).getId());
        var after = userRepository.getAll();

        assertEquals(before.size() - 1, after.size(), "Проверка size - ошибка.");
    }
}
