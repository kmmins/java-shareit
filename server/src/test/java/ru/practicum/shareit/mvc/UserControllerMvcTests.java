package ru.practicum.shareit.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    @Qualifier("userServiceImpl")
    UserService userService;

    @Test
    public void checkAddUser_Ok() throws Exception {
        UserDto userADto = new UserDto(1L, "userA", "userA@user.com");
        when(userService.add(any()))
                .thenReturn(userADto);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userADto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userADto.getId()))
                .andExpect(jsonPath("$.name").value(userADto.getName()))
                .andExpect(jsonPath("$.email").value(userADto.getEmail())
                );
    }

    /*@Test
    public void checkAddUser_NotValidThrow400() throws Exception {
        UserDto userNotValidDto = new UserDto();
        userNotValidDto.setEmail("userD@user.com");

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userNotValidDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }*/

    @Test
    public void checkGetAllUser_Ok() throws Exception {
        UserDto userADto = new UserDto(1L, "userA", "userA@user.com");
        UserDto userBDto = new UserDto(2L, "userB", "userB@user.com");
        UserDto userCDto = new UserDto(3L, "userC", "userC@user.com");
        List<UserDto> list = List.of(userADto, userBDto, userCDto);
        when(userService.getAll())
                .thenReturn(list);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void checkGetUserById_Ok() throws Exception {
        Long userId = 1L;
        UserDto userADto = new UserDto(userId, "userA", "userA@user.com");
        when(userService.getById(userId))
                .thenReturn(userADto);

        mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userADto.getId()))
                .andExpect(jsonPath("$.name").value(userADto.getName()))
                .andExpect(jsonPath("$.email").value(userADto.getEmail()));
    }

    @Test
    public void checkGetUserById_Throw404() throws Exception {
        Long wrongId = 100500L;
        when(userService.getById(wrongId))
                .thenThrow(new NotFoundException(String.format("Не найден пользователь с id: %d.", wrongId)));

        mockMvc.perform(get("/users/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(String.format("Не найден пользователь с id: %d.", wrongId),
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void checkUpdateUser_Ok() throws Exception {
        Long userId = 1L;
        UserDto userAUpdDto = new UserDto();
        userAUpdDto.setEmail("userAUpd@user.com");
        when(userService.updated(userId, userAUpdDto))
                .thenReturn(userAUpdDto);

        mockMvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(userAUpdDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userAUpdDto.getEmail()));
    }

    @Test
    public void checkUpdateUser_DuplicateEmailThrow409() throws Exception {
        Long userId = 1L;
        UserDto wrongEmail = new UserDto();
        wrongEmail.setEmail("userA@user.com");
        when(userService.updated(userId, wrongEmail))
                .thenThrow(new AlreadyExistException(String.format("Email %s уже существует.", wrongEmail.getEmail())));

        mockMvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(wrongEmail))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AlreadyExistException))
                .andExpect(result -> assertEquals(String.format("Email %s уже существует.", wrongEmail.getEmail()),
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void checkDeleteUser_Ok() throws Exception {
        Long userId = 1L;
        UserDto userADto = new UserDto(userId, "userA", "userA@user.com");
        when(userService.getById(userId))
                .thenReturn(userADto);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());
    }
}
