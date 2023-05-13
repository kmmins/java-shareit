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
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.DateTimeMatcher;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    @Qualifier("itemServiceImpl")
    ItemService itemService;

    @Test
    public void checkAddItem_Ok() throws Exception {
        Long userId = 1L;
        ItemDto itemADto = new ItemDto(null, "A", "AAA", true, null, null, null, null);
        ItemDto itemADtoAfter = new ItemDto(1L, "A", "AAA", true, null, null, null, null);
        when(itemService.add(userId, itemADto))
                .thenReturn(itemADtoAfter);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemADto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemADtoAfter.getId()))
                .andExpect(jsonPath("$.name").value(itemADtoAfter.getName()))
                .andExpect(jsonPath("$.description").value(itemADtoAfter.getDescription()))
                .andExpect(jsonPath("$.available").value(itemADtoAfter.getAvailable()));
    }

    @Test
    public void checkAddItem_NotValidThrow400() throws Exception {
        Long userId = 1L;
        ItemDto notValid = new ItemDto(null, "A", null, null, null, null, null, null);

        mockMvc.perform(post("/items", notValid)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(notValid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void checkGetAllItem_Ok() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 20;
        ItemDto itemADto = new ItemDto(null, "A", "AAA", true, null, null, null, null);
        ItemDto itemBDto = new ItemDto(null, "B", "BBB", true, null, null, null, null);
        ItemDto itemCDto = new ItemDto(null, "C", "CCC", true, null, null, null, null);
        List<ItemDto> list = List.of(itemADto, itemBDto, itemCDto);
        when(itemService.getAllItemsByOwnerId(userId, from, size))
                .thenReturn(list);

        mockMvc.perform(get("/items?from={from}&size={size}", from, size)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void checkGetByIdItem_Ok() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        List<CommentDto> commentList = new ArrayList<>();
        ItemDto itemADto = new ItemDto(1L, "A", "AAA", true, null, null, null, commentList);
        when(itemService.getById(userId, itemId))
                .thenReturn(itemADto);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemADto.getId()))
                .andExpect(jsonPath("$.name").value(itemADto.getName()))
                .andExpect(jsonPath("$.description").value(itemADto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemADto.getAvailable()))
                .andExpect(jsonPath("$.lastBooking").value(itemADto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemADto.getNextBooking()))
                .andExpect(jsonPath("$.comments").value(itemADto.getComments()));
    }

    @Test
    public void checkUpdateItem_Ok() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto itemAUpdDto = new ItemDto(null, "AUpd", "AAAUpd", false, null, null, null, null);
        ItemDto itemAUpdDtoAfter = new ItemDto(itemId, "AUpd", "AAAUpd", false, null, null, null, null);
        when(itemService.updated(userId, itemId, itemAUpdDto))
                .thenReturn(itemAUpdDtoAfter);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemAUpdDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemAUpdDtoAfter.getId()))
                .andExpect(jsonPath("$.name").value(itemAUpdDtoAfter.getName()))
                .andExpect(jsonPath("$.description").value(itemAUpdDtoAfter.getDescription()))
                .andExpect(jsonPath("$.available").value(itemAUpdDtoAfter.getAvailable()))
                .andExpect(jsonPath("$.lastBooking").value(itemAUpdDtoAfter.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemAUpdDtoAfter.getNextBooking()))
                .andExpect(jsonPath("$.comments").value(itemAUpdDtoAfter.getComments()));
    }

    @Test
    public void checkSearchItem_Ok() throws Exception {
        Long userId = 1L;
        String text = "CCC";
        int from = 0;
        int size = 20;
        ItemDto itemCDto = new ItemDto(userId, "C", "CCC", true, null, null, null, null);
        List<ItemDto> list = List.of(itemCDto);
        when(itemService.search(text, from, size))
                .thenReturn(list);

        mockMvc.perform(get("/items/search?text={text}&from={from}&size={size}", text, from, size)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(list.get(0).getName()))
                .andExpect(jsonPath("$[0].description").value(list.get(0).getDescription()))
                .andExpect(jsonPath("$[0].available").value(list.get(0).getAvailable()));
    }

    @Test
    public void checkAddComment_Ok() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        Long commentId = 1L;
        String authorName = "commentAuthor";
        CommentDto commentDto = new CommentDto();
        commentDto.setText("commentText");
        CommentDto commentDtoAfter = new CommentDto();
        commentDtoAfter.setId(commentId);
        commentDtoAfter.setText("commentText");
        commentDtoAfter.setAuthorName(authorName);
        commentDtoAfter.setCreated(LocalDateTime.now());
        when(itemService.addComment(userId, itemId, commentDto))
                .thenReturn(commentDtoAfter);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDtoAfter.getId()))
                .andExpect(jsonPath("$.text").value(commentDtoAfter.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDtoAfter.getAuthorName()))
                .andExpect(jsonPath("$.created", new DateTimeMatcher(commentDtoAfter.getCreated())));
    }
}
