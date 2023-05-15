package ru.practicum.shareit.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;
import ru.practicum.shareit.util.PageHelper;

import javax.persistence.TypedQuery;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryJpaTests {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRepositoryJpa itemRepositoryJpa;
    @Autowired
    private UserRepositoryJpa userRepositoryJpa;

    @Test
    public void checkSearchByNameLikeOrDescriptionLikeIgnoreCaseAndAvailable_Ok() {
        //data
        var allUsers = userRepositoryJpa.findAll();
        UserEntity user = new UserEntity(allUsers.size() + 1L, "test", "test@user.com");
        var testUser = userRepositoryJpa.save(user);
        var allItems = itemRepositoryJpa.findAll();
        ItemEntity item = new ItemEntity(allItems.size() + 1L);
        item.setName("test");
        item.setDescription("Просто Очень Хорошая ОТВЕРТКа прям СУПЕР!");
        item.setAvailable(true);
        item.setOwnerId(testUser.getId());
        var testItem = itemRepositoryJpa.save(item);

        //test
        TypedQuery<ItemEntity> query = em.getEntityManager().createQuery(
                        "select item from ItemEntity as item " +
                        "where upper(item.name) like upper(concat('%', ?1, '%')) " +
                        "or upper(item.description) like upper(concat('%', ?1, '%')) " +
                        "and item.available = true", ItemEntity.class
        );
        ItemEntity actualName = query.setParameter(1, "TeSt").getSingleResult();
        ItemEntity actualDescription = query.setParameter(1, "отВертка").getSingleResult();

        //assert
        assertEquals(testItem.getName(), actualName.getName(), "Ошибка поиска по имени.");
        assertEquals(testItem.getDescription(), actualDescription.getDescription(), "Ошибка поиска по описанию.");
    }

    @Test
    public void checkFindAllByOwnerId_Ok() {
        //data
        var allUsers = userRepositoryJpa.findAll();
        UserEntity user = new UserEntity(allUsers.size() + 1L, "test2", "test2@user.com");
        var testUser = userRepositoryJpa.save(user);
        var allItems1 = itemRepositoryJpa.findAll();
        ItemEntity item1 = new ItemEntity(allItems1.size() + 1L);
        item1.setName("test2");
        item1.setDescription("test2_description");
        item1.setAvailable(true);
        item1.setOwnerId(testUser.getId());
        var testItem1 = itemRepositoryJpa.save(item1);
        var allItems2 = itemRepositoryJpa.findAll();
        ItemEntity item2 = new ItemEntity(allItems2.size() + 1L);
        item2.setName("test3");
        item2.setDescription("test3_description");
        item2.setAvailable(true);
        item2.setOwnerId(testUser.getId());
        var testItem2 = itemRepositoryJpa.save(item2);
        PageRequest pageRequest = PageHelper.createRequest(0, 20);

        //test
        var check = itemRepositoryJpa.findAllByOwnerId(testUser.getId(), pageRequest);

        //assert
        assertEquals(2, check.getContent().size(), "Ошибка в количестве предметов.");
        assertEquals("test2", check.getContent().get(0).getName(), "Ошибка в имени предмета.");
        assertEquals(testItem1.getDescription(), check.getContent().get(0).getDescription(),
                "Ошибка в описании предмета.");
        assertEquals("test3", check.getContent().get(1).getName(), "Ошибка в имени предмета.");
        assertEquals(testItem2.getDescription(), check.getContent().get(1).getDescription(),
                "Ошибка в описании предмета.");
    }
}
