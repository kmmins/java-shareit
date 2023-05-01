package ru.practicum.shareit.item.dto.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.CommentEntity;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.user.model.UserEntity;

import java.time.LocalDateTime;
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

    public static CommentEntity convertToModel(UserEntity user, ItemEntity item, CommentDto commentDto) {
        CommentEntity commentModel = new CommentEntity();
        commentModel.setText(commentDto.getText());
        commentModel.setItem(item);
        commentModel.setUser(user);
        commentModel.setCreated(LocalDateTime.now());
        return commentModel;
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
