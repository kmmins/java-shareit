package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Long id;
    @NotBlank(message = "не должно быть пустым.")
    private String name;
    @NotBlank(message = "не должно быть пустым.")
    private String description;
    @NotNull(message = "не должно быть null.")
    private Boolean available;
    private Long requestId;
    private BookingRequestDto lastBooking;
    private BookingRequestDto nextBooking;
    private List<CommentRequestDto> comments;
}
