package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.ItemEntity;

import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryMemImpl implements ItemRepository {

    private Long countItems = 0L;
    private final HashMap<Long, ItemEntity> itemMemoryItemBase = new HashMap<>();

    @Override
    public ItemEntity add(ItemEntity item) {
        countItems++;
        var addedItem = new ItemEntity(
                countItems,
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                null,
                null,
                null
        );
        itemMemoryItemBase.put(countItems, addedItem);
        return addedItem;
    }

    @Override
    public Page<ItemEntity> getAllItemsByOwnerId(Long userId, PageRequest pageRequest) {
        var result = itemMemoryItemBase.values().stream()
                .filter(item -> Objects.equals(item.getOwnerId(), userId))
                .collect(Collectors.toList());
        return new PageImpl<>(result);
    }

    @Override
    public ItemEntity getById(Long id) {
        return itemMemoryItemBase.get(id);
    }

    @Override
    public ItemEntity updated(ItemEntity item) {
        var updatedItem = itemMemoryItemBase.get(item.getId());
        updatedItem.setName(item.getName());
        updatedItem.setDescription(item.getDescription());
        updatedItem.setAvailable(item.getAvailable());
        itemMemoryItemBase.put(item.getId(), updatedItem);
        return updatedItem;
    }

    @Override
    public Page<ItemEntity> search(String text, PageRequest pageRequest) {
        var lcText = text.toLowerCase();
        var res = itemMemoryItemBase.values().stream()
                .filter(ItemEntity::getAvailable)
                .filter(item -> (item.getName().toLowerCase().contains(lcText) && (!lcText.isBlank())) ||
                        (item.getDescription().toLowerCase().contains(lcText) && (!lcText.isBlank()))
                )
                .collect(Collectors.toList());
        return new PageImpl<>(res);
    }
}
