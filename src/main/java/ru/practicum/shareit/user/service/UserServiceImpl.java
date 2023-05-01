package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("userRepositoryDbImpl") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean checkDuplicateEmail(UserDto checkedUser) {
        boolean duplicate = getAll().stream()
                .anyMatch(u -> Objects.equals(u.getEmail(), checkedUser.getEmail()));
        if (duplicate) {
            throw new AlreadyExistException(String.format("Email %s уже существует.", checkedUser.getEmail()));
        }
        return false;
    }

    @Override
    public UserDto add(UserDto userDto) {
        var createdUser = UserMapper.convertToModel(userDto);
        var afterCreate = userRepository.add(createdUser);
        return UserMapper.convertToDto(afterCreate);
    }

    @Override
    public List<UserDto> getAll() {
        return UserMapper.mapToDto(userRepository.getAll());
    }

    @Override
    public UserDto getById(Long userId) {
        var userGetById = userRepository.getById(userId);
        if (userGetById == null) {
            throw new NotFoundException(String.format("Не найден пользователь с id: %d.", userId));
        }
        return UserMapper.convertToDto(userGetById);
    }

    @Override
    public UserDto updated(Long userId, UserDto userDto) {
        getById(userId); // проверка на существование пользователя.
        var updatedUser = userRepository.getById(userId);
        if (userDto.getName() != null) {
            updatedUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && userDto.getEmail().equals(updatedUser.getEmail()) ||
                userDto.getEmail() != null && !checkDuplicateEmail(userDto)) {
            updatedUser.setEmail(userDto.getEmail());
        }
        var afterUpdate = userRepository.updated(updatedUser);
        return UserMapper.convertToDto(afterUpdate);
    }

    @Override
    public void deleted(Long userId) {
        getById(userId); // проверка на существование пользователя.
        userRepository.deleted(userId);
    }
}
