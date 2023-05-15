package ru.practicum.shareit.request.dto.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.RequestEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    public static RequestDto convertToDto(RequestEntity request) {
        List<ItemDto> itemsDto = new ArrayList<>();
        List<ItemEntity> items = request.getItems();
        if (items != null) {
            return new RequestDto(
                    request.getId(),
                    request.getDescription(),
                    request.getRequestOwner(),
                    request.getCreated(),
                    ItemMapper.mapToDto(items)
            );
        }
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequestOwner(),
                request.getCreated(),
                itemsDto
        );
    }

    public static RequestEntity convertToModel(Long userId, RequestDto requestDto) {
        RequestEntity requestModel = new RequestEntity();
        requestModel.setDescription(requestDto.getDescription());
        requestModel.setRequestOwner(userId);
        requestModel.setCreated(LocalDateTime.now());
        return requestModel;
    }

    public static List<RequestDto> mapToDto(List<RequestEntity> requests) {
        List<RequestDto> result = new ArrayList<>();
        for (RequestEntity r : requests) {
            result.add(convertToDto(r));
        }
        return result;
    }
}
