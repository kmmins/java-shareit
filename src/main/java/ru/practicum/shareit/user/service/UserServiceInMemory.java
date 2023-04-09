package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.exception.UserAlreadyExistException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceInMemory implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceInMemory(@Qualifier("userRepositoryInMemory") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean checkDuplicateEmail(UserDto checkedUser) {
        boolean duplicate = getAll().stream()
                .anyMatch(u -> Objects.equals(u.getEmail(), checkedUser.getEmail()));
        if (duplicate) {
            throw new UserAlreadyExistException(String.format("Email %s уже существует.", checkedUser.getEmail()));
        }
        return false;
    }

    @Override
    public UserDto add(UserDto userDto) {
        checkDuplicateEmail(userDto);
        var createdUser = UserMapper.convertToModel(userDto);
        var afterCreate = userRepository.add(createdUser);
        return UserMapper.convertToDto(afterCreate);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(UserMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(int id) {
        var userGetById = userRepository.getById(id);
        if (userGetById == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id: %d.", id));
        }
        return UserMapper.convertToDto(userGetById);
    }

    @Override
    public UserDto upd(int id, UserDto userDto) {
        getById(id); // проверка на существование.
        var updatedUser = userRepository.getById(id);
        if (userDto.getName() != null) {
            updatedUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && userDto.getEmail().equals(updatedUser.getEmail()) ||
                userDto.getEmail() != null && !checkDuplicateEmail(userDto)) {
            updatedUser.setEmail(userDto.getEmail());
        }
        var afterUpdate = userRepository.upd(id, updatedUser);
        return UserMapper.convertToDto(afterUpdate);
    }

    @Override
    public void del(int id) {
        getById(id); // проверка на существование.
        userRepository.del(id);
    }
}
