package ru.practicum.shareit.item.dto.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.CommentEntity;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static CommentDto convertToDto(CommentEntity comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getUser().getName(),
                comment.getCreated()
        );
    }

    public static List<CommentDto> mapToDto(List<CommentEntity> comments) {
        if (comments == null) {
            return null;
        }
        List<CommentDto> result = new ArrayList<>();
        for (CommentEntity c : comments) {
            result.add(convertToDto(c));
        }
        return result;
    }
}
