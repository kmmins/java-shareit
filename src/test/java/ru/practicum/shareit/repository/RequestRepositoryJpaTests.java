package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import ru.practicum.shareit.request.model.RequestEntity;
import ru.practicum.shareit.request.repository.RequestRepositoryJpa;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;
import ru.practicum.shareit.util.PageHelper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RequestRepositoryJpaTests {

    @Autowired
    private RequestRepositoryJpa requestRepositoryJpa;
    @Autowired
    private UserRepositoryJpa userRepositoryJpa;

    @BeforeEach
    public void addData() {
        UserEntity user1 = new UserEntity(1L, "name1", "name1@name.com");
        UserEntity user2 = new UserEntity(2L, "name2", "name2@name.com");
        UserEntity user3 = new UserEntity(3L, "name3", "name3@name.com");
        var testUser1 = userRepositoryJpa.save(user1);
        var testUser2 = userRepositoryJpa.save(user2);
        var testUser3 = userRepositoryJpa.save(user3);
        RequestEntity request1 = new RequestEntity();
        request1.setDescription("AAA");
        request1.setRequestOwner(testUser1.getId());
        request1.setCreated(LocalDateTime.now());
        RequestEntity request2 = new RequestEntity();
        request2.setDescription("BBB");
        request2.setRequestOwner(testUser2.getId());
        request2.setCreated(LocalDateTime.now());
        RequestEntity request3 = new RequestEntity();
        request3.setDescription("CCC");
        request3.setRequestOwner(testUser3.getId());
        request3.setCreated(LocalDateTime.now());
        RequestEntity request4 = new RequestEntity();
        request4.setDescription("DDD");
        request4.setRequestOwner(testUser3.getId());
        request4.setCreated(LocalDateTime.now());
        RequestEntity request5 = new RequestEntity();
        request5.setDescription("EEE");
        request5.setRequestOwner(testUser3.getId());
        request5.setCreated(LocalDateTime.now());
        RequestEntity request6 = new RequestEntity();
        request6.setDescription("FFF");
        request6.setRequestOwner(testUser3.getId());
        request6.setCreated(LocalDateTime.now());
        requestRepositoryJpa.save(request1);
        requestRepositoryJpa.save(request2);
        requestRepositoryJpa.save(request3);
        requestRepositoryJpa.save(request4);
        requestRepositoryJpa.save(request5);
        requestRepositoryJpa.save(request6);
    }

    @Test
    public void checkFindAllByRequestOwnerOrderByCreatedDesc_SizeOk() {
        //data
        var allUsers = userRepositoryJpa.findAll();

        //test
        var result = requestRepositoryJpa.findAllByRequestOwnerOrderByCreatedDesc(allUsers.get(0).getId());

        //assert
        assertEquals(1, result.size(), "Ошибка в количестве собственных реквестов пользователя.");
    }

    @Test
    public void checkFindAllByRequestOwnerIsNotOrderByCreatedDesc_SizeOk() {
        //data
        var allUsers = userRepositoryJpa.findAll();
        PageRequest pr = PageHelper.createRequest(0, 20);

        //test
        var result = requestRepositoryJpa.findAllByRequestOwnerIsNotOrderByCreatedDesc(allUsers.get(1).getId(), pr);

        //assert
        assertEquals(5, result.getContent().size(), "Ошибка в количестве реквестов от всех остальных пользователей.");
    }
}
