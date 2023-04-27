package ru.practicum.shareit.booking.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.user.dto.mapper.UserMapper;


import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {

    public static BookingDto convertToDto(BookingEntity booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                UserMapper.convertToDto(booking.getBooker()),
                ItemMapper.convertToDto(booking.getItem()),
                booking.getStatus()
        );
    }

    public static List<BookingDto> mapToDto(List<BookingEntity> bookings) {
        List<BookingDto> result = new ArrayList<>();
        for (BookingEntity b : bookings) {
            result.add(convertToDto(b));
        }
        return result;
    }
}
