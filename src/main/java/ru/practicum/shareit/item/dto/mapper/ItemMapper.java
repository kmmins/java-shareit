package ru.practicum.shareit.item.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public static ItemDto convertToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item convertToModel(Integer UserId, ItemDto itemDto) {
        var itemModel = new Item();
        itemModel.setName(itemDto.getName());
        itemModel.setDescription(itemDto.getDescription());
        itemModel.setAvailable(itemDto.getAvailable());
        itemModel.setOwnerId(UserId);

        return itemModel;
    }
}
