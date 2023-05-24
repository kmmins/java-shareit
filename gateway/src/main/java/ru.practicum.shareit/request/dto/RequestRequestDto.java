package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class RequestRequestDto {
    private final Long id;
    @NotBlank(message = "не должно быть пустым.")
    private String description;
    private Long requestOwner;
    private LocalDateTime created;
    private List<ItemRequestDto> items;
}
