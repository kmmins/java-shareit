package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTests {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;

    @Test
    public void checkUserAdd_Ok() {
        //data
        Long userId = 1L;
        var userDto = new UserDto(userId, "user", "user@user.com");
        when(userRepository.add(UserMapper.convertToModel(userDto)))
                .thenReturn(UserMapper.convertToModel(userDto));

        //test
        var check = userService.add(userDto);

        //assert
        assertEquals("user", check.getName(), "Некорректное имя после добавления." + check.getName());
        assertEquals("user@user.com", check.getEmail(), "Некорректная почта после добавления."
                + check.getName());
    }

    @Test
    public void checkUserUpdated_ThrowAndOk() {
        //data
        Long userId = 1L;
        Long wrongUserID = 100500L;
        var userDto = new UserDto(userId, "user", "user@user.com");
        var userDtoUpd = new UserDto(userId, "userUpd", "userUpd@user.com");
        var WrongEmail = new UserDto(userId, "WrongEmail", "userUpd@user.com");
        when(userRepository.getById(wrongUserID))
                .thenThrow(new NotFoundException(String.format("Не найден пользователь с id: %d.", wrongUserID)));
        when(userRepository.getById(userId))
                .thenReturn(UserMapper.convertToModel(userDto));
        when(userRepository.updated(UserMapper.convertToModel(userDtoUpd)))
                .thenReturn(UserMapper.convertToModel(userDtoUpd));
        when(userRepository.updated(UserMapper.convertToModel(WrongEmail)))
                .thenThrow(new AlreadyExistException(String.format("Email %s уже существует.", WrongEmail.getEmail())));

        //test
        final NotFoundException e1 = assertThrows(NotFoundException.class,
                () -> userService.updated(wrongUserID, userDto));
        var check = userService.updated(userId, userDtoUpd);
        final AlreadyExistException e2 = assertThrows(AlreadyExistException.class,
                () -> userService.updated(userId, WrongEmail));

        //assert
        assertEquals(String.format("Не найден пользователь с id: %d.", wrongUserID), e1.getMessage());
        assertEquals("userUpd", check.getName(), "Некорректное имя после обновления."
                + check.getName());
        assertEquals("userUpd@user.com", check.getEmail(), "Некорректная почта после обновления."
                + check.getName());
        assertEquals(String.format("Email %s уже существует.", WrongEmail.getEmail()), e2.getMessage());
    }

    @Test
    public void checkUserDeleted_OneTimes() {
        //data
        Long userId = 1L;
        var userDto = new UserDto(userId, "user", "user@user.com");
        when(userRepository.getById(userId))
                .thenReturn(UserMapper.convertToModel(userDto));

        //test
        userService.deleted(userId);

        //assert
        Mockito.verify(userRepository, Mockito.times(1))
                .deleted(userId);
    }
}
