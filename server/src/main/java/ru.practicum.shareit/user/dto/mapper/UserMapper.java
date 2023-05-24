package ru.practicum.shareit.user.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public static UserDto convertToDto(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static UserEntity convertToModel(UserDto userDto) {
        return new UserEntity(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static List<UserDto> mapToDto(List<UserEntity> users) {
        List<UserDto> result = new ArrayList<>();
        for (UserEntity u : users) {
            result.add(convertToDto(u));
        }
        return result;
    }
}
