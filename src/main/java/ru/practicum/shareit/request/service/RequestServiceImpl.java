package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.mapper.RequestMapper;
import ru.practicum.shareit.request.model.RequestEntity;
import ru.practicum.shareit.request.repository.RequestRepositoryJpa;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.PageHelper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepositoryJpa requestRepositoryJpa;
    private final UserService userService;

    @Autowired
    public RequestServiceImpl(RequestRepositoryJpa requestRepositoryJpa,
                              UserService userService) {
        this.requestRepositoryJpa = requestRepositoryJpa;
        this.userService = userService;
    }

    @Override
    public RequestDto add(Long userId, RequestDto requestDto) {
        userService.getById(userId);
        var addedRequest = new RequestEntity();
        addedRequest.setDescription(requestDto.getDescription());
        addedRequest.setRequestOwner(userId);
        addedRequest.setCreated(LocalDateTime.now());
        var afterCreated = requestRepositoryJpa.save(addedRequest);
        return RequestMapper.convertToDto(afterCreated);
    }

    @Override
    public List<RequestDto> getAllOwn(Long userId) {
        userService.getById(userId);
        var thisUserRequests = requestRepositoryJpa.findAllByRequestOwnerOrderByCreatedDesc(userId);
        return RequestMapper.mapToDto(thisUserRequests);
    }

    @Override
    public List<RequestDto> getAllFromSize(Long userId, int from, int size) {
        userService.getById(userId);
        PageRequest pageRequest = PageHelper.createRequest(from, size);
        Page<RequestEntity> page = requestRepositoryJpa.findAllByRequestOwnerIsNotOrderByCreatedDesc(userId, pageRequest);
        List<RequestEntity> result = page.getContent();
        return RequestMapper.mapToDto(result);
    }

    @Override
    public RequestDto getById(Long userId, Long requestId) {
        userService.getById(userId);
        RequestEntity result = requestRepositoryJpa.findById(requestId).orElse(null);
        if (result == null) {
            throw new NotFoundException(String.format("Запрос с id %d не найден.", requestId));
        }
        return RequestMapper.convertToDto(result);
    }
}
