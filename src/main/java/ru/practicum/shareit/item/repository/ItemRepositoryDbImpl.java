package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.ItemEntity;

import java.util.List;

@Repository
public class ItemRepositoryDbImpl implements ItemRepository {

    private final ItemRepositoryJpa itemRepositoryJpa;

    @Autowired
    public ItemRepositoryDbImpl(ItemRepositoryJpa itemRepositoryJpa) {
        this.itemRepositoryJpa = itemRepositoryJpa;
    }

    @Override
    public ItemEntity add(ItemEntity item) {
        return itemRepositoryJpa.save(item);
    }

    @Override
    public List<ItemEntity> getAllItemsOwnByUser(Long userId) {
        return itemRepositoryJpa.findAllByOwnerId(userId);
    }

    @Override
    public ItemEntity getById(Long id) {
        return itemRepositoryJpa.findById(id).orElse(null);
    }

    @Override
    public ItemEntity updated(ItemEntity item) {
        return itemRepositoryJpa.save(item);
    }

    @Override
    public List<ItemEntity> search(String text) {
        return itemRepositoryJpa.searchByNameLikeOrDescriptionLikeIgnoreCaseAndAvailable(text);
    }
}
