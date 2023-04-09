package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemory implements ItemRepository {
    private int countItems = 0;
    private final HashMap<Integer, Item> itemMemoryItemBase = new HashMap<>();

    @Override
    public Item add(Item item) {
        countItems++;
        var addedItem = new Item(
                countItems,
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                null
        );
        itemMemoryItemBase.put(countItems, addedItem);
        return addedItem;
    }

    @Override
    public List<Item> getAll(int userId) {
        return itemMemoryItemBase.values().stream()
                .filter(item -> Objects.equals(item.getOwnerId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(int id) {
        return itemMemoryItemBase.get(id);
    }

    @Override
    public Item upd(int itemId, Item item) {
        var updatedItem = itemMemoryItemBase.get(itemId);
        updatedItem.setName(item.getName());
        updatedItem.setDescription(item.getDescription());
        updatedItem.setAvailable(item.getAvailable());
        itemMemoryItemBase.put(itemId, updatedItem);
        return updatedItem;
    }

    @Override
    public List<Item> getFound(String text) {
        var result = itemMemoryItemBase.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> (item.getName().toLowerCase().contains(text) && (!text.isEmpty() || !text.isBlank())) ||
                        (item.getDescription().toLowerCase().contains(text) && (!text.isEmpty() || !text.isBlank()))
                )
                .collect(Collectors.toList());

        return result;
    }
}
