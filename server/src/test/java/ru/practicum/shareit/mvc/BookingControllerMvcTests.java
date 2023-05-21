package ru.practicum.shareit.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.DateTimeMatcher;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    BookingService bookingService;

    @Test
    public void checkAddBooking_Ok() throws Exception {
        Long userId = 1L;
        Long itemId = 2L;
        Long bookerId = 3L;
        Long bookingId = 100500L;
        BookingDto bookingDto = new BookingDto(
                null,
                itemId,
                null,
                LocalDateTime.of(2023, 10, 9, 8, 7, 0),
                LocalDateTime.of(2023, 10, 9, 8, 7, 1),
                null,
                null,
                null);
        BookingDto bookingDtoAfter = new BookingDto(
                bookingId,
                itemId,
                bookerId,
                LocalDateTime.of(2023, 10, 9, 8, 7, 0),
                LocalDateTime.of(2023, 10, 9, 8, 7, 1),
                new UserDto(bookerId, "booker", "booker@booker.com"),
                new ItemDto(itemId, "D", "DDD", true, null, null, null, new ArrayList<>()),
                Status.WAITING
        );
        when(bookingService.add(userId, bookingDto))
                .thenReturn(bookingDtoAfter);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoAfter.getId()))
                .andExpect(jsonPath("$.itemId").value(bookingDtoAfter.getItemId()))
                .andExpect(jsonPath("$.bookerId").value(bookingDtoAfter.getBookerId()))
                .andExpect(jsonPath("$.start", new DateTimeMatcher(bookingDtoAfter.getStart())))
                .andExpect(jsonPath("$.end", new DateTimeMatcher(bookingDtoAfter.getEnd())))
                .andExpect(jsonPath("$.booker").value(bookingDtoAfter.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDtoAfter.getItem()))
                .andExpect(jsonPath("$.status").value(bookingDtoAfter.getStatus().toString()));
    }

    @Test
    public void checkApproveBooking_Ok() throws Exception {
        Long userId = 1L;
        Long itemId = 2L;
        Long bookerId = 3L;
        Long bookingId = 100500L;
        boolean approved = true;
        BookingDto bookingDto = new BookingDto(
                null,
                itemId,
                null,
                LocalDateTime.of(2023, 10, 9, 8, 7, 0),
                LocalDateTime.of(2023, 10, 9, 8, 7, 1),
                null,
                null,
                null);
        BookingDto bookingDtoAfter = new BookingDto(
                bookingId,
                itemId,
                bookerId,
                LocalDateTime.of(2023, 10, 9, 8, 7, 0),
                LocalDateTime.of(2023, 10, 9, 8, 7, 1),
                new UserDto(bookerId, "booker", "booker@booker.com"),
                new ItemDto(itemId, "D", "DDD", true, null, null, null, new ArrayList<>()),
                Status.APPROVED);
        when(bookingService.approve(userId, bookingId, approved))
                .thenReturn(bookingDtoAfter);

        mockMvc.perform(patch("/bookings/{bookingId}?approved={approved}", bookingId, approved)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoAfter.getId()))
                .andExpect(jsonPath("$.itemId").value(bookingDtoAfter.getItemId()))
                .andExpect(jsonPath("$.bookerId").value(bookingDtoAfter.getBookerId()))
                .andExpect(jsonPath("$.start", new DateTimeMatcher(bookingDtoAfter.getStart())))
                .andExpect(jsonPath("$.end", new DateTimeMatcher(bookingDtoAfter.getEnd())))
                .andExpect(jsonPath("$.booker").value(bookingDtoAfter.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDtoAfter.getItem()))
                .andExpect(jsonPath("$.status").value(bookingDtoAfter.getStatus().toString()));
    }

    @Test
    public void checkGetByIdBooking_Ok() throws Exception {
        Long userId = 3L;
        Long itemId = 2L;
        Long bookerId = 3L;
        Long bookingId = 100500L;
        BookingDto bookingDtoAfter = new BookingDto(
                bookingId,
                itemId,
                bookerId,
                LocalDateTime.of(2023, 10, 9, 8, 7, 0),
                LocalDateTime.of(2023, 10, 9, 8, 7, 1),
                new UserDto(bookerId, "booker", "booker@booker.com"),
                new ItemDto(itemId, "D", "DDD", true, null, null, null, new ArrayList<>()),
                Status.APPROVED);
        when(bookingService.getById(userId, bookingId))
                .thenReturn(bookingDtoAfter);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoAfter.getId()))
                .andExpect(jsonPath("$.itemId").value(bookingDtoAfter.getItemId()))
                .andExpect(jsonPath("$.bookerId").value(bookingDtoAfter.getBookerId()))
                .andExpect(jsonPath("$.start", new DateTimeMatcher(bookingDtoAfter.getStart())))
                .andExpect(jsonPath("$.end", new DateTimeMatcher(bookingDtoAfter.getEnd())))
                .andExpect(jsonPath("$.booker").value(bookingDtoAfter.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDtoAfter.getItem()))
                .andExpect(jsonPath("$.status").value(bookingDtoAfter.getStatus().toString()));
    }

    @Test
    public void checkGetAllBookingsForUser_Ok() throws Exception {
        Long userId = 1L;
        Long itemId = 2L;
        Long bookerId = 1L;
        Long bookingId = 100500L;
        int from = 0;
        int size = 20;
        State state = State.ALL;
        BookingDto bookingDtoAfter = new BookingDto(
                bookingId,
                itemId,
                bookerId,
                LocalDateTime.of(2023, 10, 9, 8, 7, 0),
                LocalDateTime.of(2023, 10, 9, 8, 7, 1),
                new UserDto(bookerId, "booker", "booker@booker.com"),
                new ItemDto(itemId, "D", "DDD", true, null, null, null, new ArrayList<>()),
                Status.APPROVED);
        var list = List.of(bookingDtoAfter);
        when(bookingService.getAllBookingForUser(userId, state, from, size))
                .thenReturn(list);

        mockMvc.perform(get("/bookings?state={state}&from={from}&size={size}", state, from, size)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookingDtoAfter.getId()))
                .andExpect(jsonPath("$[0].itemId").value(bookingDtoAfter.getItemId()))
                .andExpect(jsonPath("$[0].bookerId").value(bookingDtoAfter.getBookerId()))
                .andExpect(jsonPath("$[0].start", new DateTimeMatcher(bookingDtoAfter.getStart())))
                .andExpect(jsonPath("$[0].end", new DateTimeMatcher(bookingDtoAfter.getEnd())))
                .andExpect(jsonPath("$[0].booker").value(bookingDtoAfter.getBooker()))
                .andExpect(jsonPath("$[0].item").value(bookingDtoAfter.getItem()))
                .andExpect(jsonPath("$[0].status").value(bookingDtoAfter.getStatus().toString()));
    }

    @Test
    public void checkGetAllBookingsForOwner_Ok() throws Exception {
        Long userId = 1L;
        Long itemId = 2L;
        Long bookerId = 3L;
        Long bookingId = 100500L;
        int from = 0;
        int size = 20;
        State state = State.ALL;
        BookingDto bookingDtoAfter = new BookingDto(
                bookingId,
                itemId,
                bookerId,
                LocalDateTime.of(2023, 10, 9, 8, 7, 0),
                LocalDateTime.of(2023, 10, 9, 8, 7, 1),
                new UserDto(bookerId, "booker", "booker@booker.com"),
                new ItemDto(itemId, "D", "DDD", true, null, null, null, new ArrayList<>()),
                Status.APPROVED);
        var list = List.of(bookingDtoAfter);
        when(bookingService.getAllBookingForOwnerItems(userId, state, from, size))
                .thenReturn(list);

        mockMvc.perform(get("/bookings/owner?state={state}&from={from}&size={size}", state, from, size)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookingDtoAfter.getId()))
                .andExpect(jsonPath("$[0].itemId").value(bookingDtoAfter.getItemId()))
                .andExpect(jsonPath("$[0].bookerId").value(bookingDtoAfter.getBookerId()))
                .andExpect(jsonPath("$[0].start", new DateTimeMatcher(bookingDtoAfter.getStart())))
                .andExpect(jsonPath("$[0].end", new DateTimeMatcher(bookingDtoAfter.getEnd())))
                .andExpect(jsonPath("$[0].booker").value(bookingDtoAfter.getBooker()))
                .andExpect(jsonPath("$[0].item").value(bookingDtoAfter.getItem()))
                .andExpect(jsonPath("$[0].status").value(bookingDtoAfter.getStatus().toString()));
    }

    @Test
    public void checkGetAllBookingsForOwner_WrongStateThrow400() throws Exception {
        String wrongState = "wrongState";
        Long userId = 1L;
        int from = 0;
        int size = 20;

        mockMvc.perform(get("/bookings/owner?state={state}&from={from}&size={size}", wrongState, from, size)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ParameterException))
                .andExpect(result -> assertEquals("Unknown state: " + wrongState,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
