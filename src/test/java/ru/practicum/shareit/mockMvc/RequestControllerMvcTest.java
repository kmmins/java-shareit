package ru.practicum.shareit.mockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.util.DateTimeMatcher;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    RequestService requestService;

    @Test
    public void checkAddRequest_Ok() throws Exception {
        Long userId = 1L;
        Long requestId = 1L;
        Long requestOwner = 1L;
        RequestDto requestDto = new RequestDto(null, "AAA", null, null, null);
        RequestDto requestDtoAfter = new RequestDto(
                requestId, "AAA", requestOwner, LocalDateTime.now(), new ArrayList<>()
        );
        when(requestService.add(userId, requestDto))
                .thenReturn(requestDtoAfter);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestDtoAfter.getId()))
                .andExpect(jsonPath("$.description").value(requestDtoAfter.getDescription()))
                .andExpect(jsonPath("$.requestOwner").value(requestDtoAfter.getRequestOwner()))
                .andExpect(jsonPath("$.created", new DateTimeMatcher(requestDtoAfter.getCreated())))
                .andExpect(jsonPath("$.items").value(requestDtoAfter.getItems()));
    }

    @Test
    void checkAddRequest_NotValidThrow400() throws Exception {
        Long userId = 1L;
        Long requestId = 1L;
        Long requestOwner = 1L;
        RequestDto notValid = new RequestDto(
                requestId, null, requestOwner, LocalDateTime.now(), new ArrayList<>()
        );

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(notValid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void checkGetAllOwnRequest_Ok() throws Exception {
        Long userId = 1L;
        RequestDto requestADto = new RequestDto(null, "AAA", null, null, null);
        RequestDto requestBDto = new RequestDto(null, "BBB", null, null, null);
        RequestDto requestCDto = new RequestDto(null, "CCC", null, null, null);
        List<RequestDto> list = List.of(requestADto, requestBDto, requestCDto);
        when(requestService.getAllOwn(userId))
                .thenReturn(list);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void checkGetAllRequestPageable_Ok() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 20;
        RequestDto requestADto = new RequestDto(null, "AAA", null, null, null);
        RequestDto requestBDto = new RequestDto(null, "BBB", null, null, null);
        List<RequestDto> list = List.of(requestADto, requestBDto);
        when(requestService.getAllFromSize(userId, from, size))
                .thenReturn(list);

        mockMvc.perform(get("/requests/all?from={from}&size={size}", from, size)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void checkGetRequestById_Ok() throws Exception {
        Long userId = 1L;
        Long requestId = 100500L;
        RequestDto requestADto = new RequestDto(requestId, "AAA", userId, LocalDateTime.now(), new ArrayList<>());
        when(requestService.getById(anyLong(), anyLong()))
                .thenReturn(requestADto);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestADto.getId()))
                .andExpect(jsonPath("$.description").value(requestADto.getDescription()))
                .andExpect(jsonPath("$.requestOwner").value(requestADto.getRequestOwner()))
                .andExpect(jsonPath("$.created", new DateTimeMatcher(requestADto.getCreated())))
                .andExpect(jsonPath("$.items").value(requestADto.getItems()));
    }
}
