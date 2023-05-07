package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.ItemEntity;

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
    public Page<ItemEntity> getAllItemsByOwnerId(Long userId, PageRequest pageRequest) {
        return itemRepositoryJpa.findAllByOwnerId(userId, pageRequest);
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
    public Page<ItemEntity> search(String text, PageRequest pageRequest) {
        return itemRepositoryJpa.searchByNameLikeOrDescriptionLikeIgnoreCaseAndAvailable(text, pageRequest);
    }
}
