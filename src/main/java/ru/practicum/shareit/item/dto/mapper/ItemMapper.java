package ru.practicum.shareit.item.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.user.dto.mapper.UserMapper;

import java.util.*;
import java.time.LocalDateTime;

@Component
public class ItemMapper {

    public static ItemDto convertToDto(ItemEntity item) {
        return convertToDto(item, false);
    }

    public static ItemDto convertToDto(ItemEntity item, boolean isOwner) {
        var listBookingForThisItem = item.getBookingEntityList();
        BookingDto lastDto = null;
        BookingDto nextDto = null;
        if (isOwner && listBookingForThisItem != null) {
            var last = listBookingForThisItem.stream()
                    .filter(b -> b.getStartTime().isBefore(LocalDateTime.now()))
                    .filter(b -> b.getStatus() == Status.APPROVED)
                    .max(Comparator.comparing(BookingEntity::getEndTime))
                    .orElse(null);
            var next = listBookingForThisItem.stream()
                    .filter(b -> b.getStartTime().isAfter(LocalDateTime.now()))
                    .filter(b -> b.getStatus() == Status.APPROVED)
                    .min(Comparator.comparing(BookingEntity::getStartTime))
                    .orElse(null);
            if (last != null) {
                lastDto = new BookingDto(
                        last.getId(),
                        last.getItem().getId(),
                        last.getBooker().getId(),
                        last.getStartTime(),
                        last.getEndTime(),
                        UserMapper.convertToDto(last.getBooker()),
                        null,
                        last.getStatus()
                );
            }
            if (next != null) {
                nextDto = new BookingDto(
                        next.getId(),
                        next.getItem().getId(),
                        next.getBooker().getId(),
                        next.getStartTime(),
                        next.getEndTime(),
                        UserMapper.convertToDto(next.getBooker()),
                        null,
                        next.getStatus()
                );
            }
        }
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastDto,
                nextDto,
                CommentMapper.mapToDto(item.getCommentEntityList())
        );
    }

    public static ItemEntity convertToModel(Long userId, ItemDto itemDto) {
        ItemEntity itemModel = new ItemEntity();
        itemModel.setName(itemDto.getName());
        itemModel.setDescription(itemDto.getDescription());
        itemModel.setAvailable(itemDto.getAvailable());
        itemModel.setOwnerId((userId));
        return itemModel;
    }

    public static List<ItemDto> mapToDto(List<ItemEntity> items) {
        List<ItemDto> result = new ArrayList<>();
        for (ItemEntity i : items) {
            result.add(convertToDto(i, true));
        }
        return result;
    }
}
