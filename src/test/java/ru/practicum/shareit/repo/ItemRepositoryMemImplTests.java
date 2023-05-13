package ru.practicum.shareit.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepositoryMemImpl;
import ru.practicum.shareit.util.PageHelper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemRepositoryMemImplTests {

    @Autowired
    private ItemRepositoryMemImpl itemRepository;


    @BeforeEach
    public void addData() {
        var item0 = new ItemEntity();
        item0.setName("object0");
        item0.setDescription("object0_description");
        item0.setAvailable(true);
        item0.setOwnerId(1L);
        itemRepository.add(item0);
    }

    @Test
    public void checkItemAddMem() {
        var item1 = new ItemEntity();
        item1.setName("object1");
        item1.setDescription("object1_description");
        item1.setAvailable(true);
        item1.setOwnerId(1L);
        PageRequest pageRequest = PageHelper.createRequest(0, 20);
        var before = itemRepository.getAllItemsByOwnerId(1L, pageRequest);

        var result = itemRepository.add(item1);

        assertEquals(before.getContent().size() + 1, result.getId(), "Проверка size - ошибка.");
        assertEquals("object1", result.getName(), "Проверка name - ошибка.");
        assertEquals("object1_description", result.getDescription(), "Проверка description - ошибка.");
        assertEquals(true, result.getAvailable(), "Проверка available - ошибка.");
    }

    @Test
    public void checkItemGetByIdMem() {
        var result = itemRepository.getById(1L);

        assertEquals("object0", result.getName(), "Проверка name - ошибка.");
        assertEquals("object0_description", result.getDescription(), "Проверка description - ошибка.");
        assertEquals(true, result.getAvailable(), "Проверка available - ошибка.");
    }

    @Test
    public void checkItemUpdatedMem() {
        var toUpd = itemRepository.getById(1L);
        toUpd.setName("object0Upd");
        toUpd.setDescription("object0Upd_description");
        toUpd.setAvailable(false);

        var result = itemRepository.updated(toUpd);

        assertEquals("object0Upd", result.getName(), "Проверка name - ошибка.");
        assertEquals("object0Upd_description", result.getDescription(), "Проверка description - ошибка.");
        assertEquals(false, result.getAvailable(), "Проверка available - ошибка.");
    }

    @Test
    public void checkItemSearchMem() {
        String text1 = "obJeCt";
        String text2 = "description";
        PageRequest pageRequest = PageHelper.createRequest(0, 1);

        var page1 = itemRepository.search(text1, pageRequest);
        var result1 = page1.getContent().get(0);
        var page2 = itemRepository.search(text2, pageRequest);
        var result2 = page2.getContent().get(0);

        assertEquals("object0", result1.getName(), "Проверка name - ошибка.");
        assertEquals("object0_description", result1.getDescription(), "Проверка description - ошибка.");
        assertEquals(true, result1.getAvailable(), "Проверка available - ошибка.");
        assertNotNull(result2, "Ошибка - null.");
    }
}
