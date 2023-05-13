package ru.practicum.shareit.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepositoryDbImpl;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepositoryDbImpl userRepository;
    @Autowired
    private UserRepositoryJpa userRepositoryJpa;

    @BeforeEach
    public void addData() {
        UserEntity user1 = new UserEntity(1L, "user1", "user1@user.com");
        UserEntity user2 = new UserEntity(2L, "user2", "user2@user.com");
        UserEntity user3 = new UserEntity(3L, "user3", "user3@user.com");
        userRepositoryJpa.save(user1);
        userRepositoryJpa.save(user2);
        userRepositoryJpa.save(user3);
    }

    @Test
    public void checkUserAdd_Ok() {
        //data
        var listUsers = userRepositoryJpa.findAll();
        UserEntity userAdd = new UserEntity(4L, "userAdd", "userAdd@user.com");

        //test
        var check = userRepository.add(userAdd);

        //assert
        assertEquals(listUsers.size() + 1, check.getId(), "Проверка id - ошибка.");
        assertEquals("userAdd", check.getName(), "Проверка name - ошибка.");
        assertEquals("userAdd@user.com", check.getEmail(), "Проверка email - ошибка.");
    }

    @Test
    public void checkUserGetById_Ok() {
        //data
        var listUsers = userRepositoryJpa.findAll();

        //test
        var check = userRepository.getById(listUsers.get(1).getId());

        //assert
        assertEquals(listUsers.get(1).getId(), check.getId(), "Проверка id - ошибка.");
        assertEquals("user2", check.getName(), "Проверка name - ошибка.");
        assertEquals("user2@user.com", check.getEmail(), "Проверка email - ошибка.");
    }

    @Test
    public void checkUserGetAllAndDelete_SizeOk() {
        //data
        var check = userRepositoryJpa.findAll();

        //test
        userRepository.deleted((long) check.size());
        var checkAfter = userRepository.getAll();

        //assert
        assertEquals(check.size() - 1, checkAfter.size(), "Некорректное общее количество пользователей после удаления.");
    }

    @Test
    public void checkUserUpdated_Ok() {
        //data
        var listUsers = userRepositoryJpa.findAll();
        listUsers.get(0).setName("userUpd");
        listUsers.get(0).setEmail("userUpd@user.com");

        //test
        var check = userRepository.updated(listUsers.get(0));

        //assert
        assertEquals(listUsers.get(0).getId(), check.getId(), "Проверка id - ошибка.");
        assertEquals("userUpd", check.getName(), "Проверка name - ошибка.");
        assertEquals("userUpd@user.com", check.getEmail(), "Проверка email - ошибка.");
    }
}
