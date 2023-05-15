package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.mapper.RequestMapper;
import ru.practicum.shareit.request.model.RequestEntity;
import ru.practicum.shareit.request.repository.RequestRepositoryJpa;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.PageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplUnitTests {

    @InjectMocks
    RequestServiceImpl requestService;
    @Mock
    RequestRepositoryJpa requestRepositoryJpa;
    @Mock
    UserService userService;

    @Test
    public void checkRequestAdd_IdOk() {
        //data
        Long userId = 1L;
        Long requestId = 1L;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var requestDto = new RequestDto();
        requestDto.setDescription("RRR");
        var requestModel = RequestMapper.convertToModel(userId, requestDto);
        var afterAdd = new RequestEntity(requestId);
        afterAdd.setDescription(requestModel.getDescription());
        afterAdd.setRequestOwner(requestModel.getRequestOwner());
        afterAdd.setCreated(requestModel.getCreated());
        when(userService.getById(userId))
                .thenReturn(userDto);
        when(requestRepositoryJpa.save(any(RequestEntity.class)))
                .thenReturn(afterAdd);

        //test
        var check = requestService.add(userId, requestDto);

        //assert
        assertEquals(requestId, check.getId(), "Некорректный id реквеста.");
    }

    @Test
    public void checkRequestGetAllOwn_SizeOk() {
        //data
        Long userId = 1L;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var one = new RequestEntity();
        one.setItems(new ArrayList<>());
        var two = new RequestEntity();
        two.setItems(new ArrayList<>());
        List<RequestEntity> list = List.of(one, two);
        when(userService.getById(userId))
                .thenReturn(userDto);
        when(requestRepositoryJpa.findAllByRequestOwnerOrderByCreatedDesc(userId))
                .thenReturn(list);

        //test
        var check = requestService.getAllOwn(userId);

        //assert
        assertEquals(2, check.size());
    }

    @Test
    public void getAllFromSize_SizeOk() {
        //data
        Long userId = 1L;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var one = new RequestEntity();
        List<RequestEntity> list = List.of(one);
        Page<RequestEntity> page = new PageImpl<>(list);
        PageRequest pageRequest = PageHelper.createRequest(0, 20);
        when(userService.getById(userId))
                .thenReturn(userDto);
        when(requestRepositoryJpa.findAllByRequestOwnerIsNotOrderByCreatedDesc(userId, pageRequest))
                .thenReturn(page);

        //test
        var check = requestService.getAllFromSize(userId, 0, 20);

        //assert
        assertEquals(1, check.size());
    }

    @Test
    public void getById_IdOkAndNotFound() {
        //data
        Long userId = 1L;
        Long requestId = 100500L;
        Long wrongRequestId = 500100L;
        var userDto = new UserDto(userId, "name", "name@name.com");
        var requestDto = new RequestDto();
        requestDto.setDescription("RRR");
        var afterFind = new RequestEntity(requestId);
        when(userService.getById(userId))
                .thenReturn(userDto);
        when(requestRepositoryJpa.findById(requestId))
                .thenReturn(Optional.of(afterFind));
        when(requestRepositoryJpa.findById(wrongRequestId))
                .thenReturn(Optional.empty());

        //test
        var check = requestService.getById(userId, requestId);
        final NotFoundException e = assertThrows(NotFoundException.class,
                () -> requestService.getById(userId, wrongRequestId));

        //assert
        assertEquals(requestId, check.getId(), "Некорректный id реквеста.");
        assertEquals(String.format("Запрос с id %d не найден.", wrongRequestId), e.getMessage());
    }
}
