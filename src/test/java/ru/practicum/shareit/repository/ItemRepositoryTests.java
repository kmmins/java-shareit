package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepositoryDbImpl;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;
import ru.practicum.shareit.util.PageHelper;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=validate"})
public class ItemRepositoryTests {

    @Autowired
    private ItemRepositoryDbImpl itemRepository;
    @Autowired
    private ItemRepositoryJpa itemRepositoryJpa;
    @Autowired
    private UserRepositoryJpa userRepositoryJpa;

    @BeforeEach
    public void addData() {
        UserEntity user1 = new UserEntity(1L, "user1", "user1@user.com");
        UserEntity user2 = new UserEntity(2L, "user2", "user2@user.com");
        UserEntity user3 = new UserEntity(3L, "user3", "user3@user.com");
        var testUser1 = userRepositoryJpa.save(user1);
        var testUser2 = userRepositoryJpa.save(user2);
        var testUser3 = userRepositoryJpa.save(user3);
        ItemEntity item1 = new ItemEntity();
        item1.setName("item1");
        item1.setDescription("item1_description");
        item1.setAvailable(true);
        item1.setOwnerId(testUser1.getId());
        ItemEntity item2 = new ItemEntity();
        item2.setName("item2");
        item2.setDescription("item2_description");
        item2.setAvailable(true);
        item2.setOwnerId(testUser2.getId());
        ItemEntity item3 = new ItemEntity();
        item3.setName("item3");
        item3.setDescription("item3_description");
        item3.setAvailable(true);
        item3.setOwnerId(testUser3.getId());
        itemRepository.add(item1);
        itemRepository.add(item2);
        itemRepository.add(item3);
    }

    @Test
    public void checkItemAddItems_Ok() {
        //data
        ItemEntity itemAdd = new ItemEntity();
        itemAdd.setName("itemAdd");
        itemAdd.setDescription("itemAdd_description");
        itemAdd.setAvailable(true);
        itemAdd.setOwnerId(3L);
        var allItems = itemRepositoryJpa.findAll().stream()
                .max((Comparator.comparing(ItemEntity::getId)))
                .orElse(null);
        long expectedId;
        if (allItems != null) {
            expectedId = allItems.getId() + 1L;
        } else {
            expectedId = 1L;
        }

        //test
        var check = itemRepository.add(itemAdd);

        //assert
        assertEquals(expectedId, check.getId(), "Проверка id - ошибка.");
        assertEquals("itemAdd", check.getName(), "Проверка name - ошибка.");
        assertEquals("itemAdd_description", check.getDescription(), "Проверка description - ошибка.");
        assertEquals(3L, check.getOwnerId(), "Проверка OwnerId - ошибка.");

    }

    @Test
    public void checkGetById_Ok() {
        //data
        var all = itemRepositoryJpa.findAll();

        //test
        var check = itemRepository.getById(all.get(2).getId());

        //assert
        assertEquals(all.get(2).getId(), check.getId(), "Проверка id - ошибка.");
        assertEquals(all.get(2).getName(), check.getName(), "Проверка name - ошибка.");
        assertEquals(all.get(2).getDescription(), check.getDescription(), "Проверка description - ошибка.");
        assertEquals(all.get(2).getOwnerId(), check.getOwnerId(), "Проверка owner_id - ошибка.");
    }

    @Test
    public void checkGetAllItemsByOwnerId_Ok() {
        //data
        Long userId = 3L;
        PageRequest pageRequest = PageHelper.createRequest(0, 20);
        var itemsOwnByUserId = itemRepositoryJpa.findAll().stream()
                .filter(i -> Objects.equals(i.getOwnerId(), userId))
                .collect(Collectors.toList());

        //test
        var check = itemRepository.getAllItemsByOwnerId(userId, pageRequest);

        //assert
        assertEquals(itemsOwnByUserId.size(), check.getContent().size(), "Проверка size - ошибка.");
        assertEquals(itemsOwnByUserId.get(0).getName(), check.getContent().get(0).getName(), "Проверка name - ошибка.");
    }

    @Test
    public void checkUpdated_Ok() {
        //data
        var all = itemRepositoryJpa.findAll();
        var itemUpd = all.get(2);
        itemUpd.setName("itemUpd");
        itemUpd.setDescription("itemUpd_description");
        itemUpd.setAvailable(false);

        //test
        var check = itemRepository.updated(itemUpd);

        //assert
        assertEquals("itemUpd", check.getName(), "Проверка name - ошибка.");
        assertEquals("itemUpd_description", check.getDescription(), "Проверка description - ошибка.");
        assertEquals(false, check.getAvailable(), "Проверка available - ошибка.");
    }
}
