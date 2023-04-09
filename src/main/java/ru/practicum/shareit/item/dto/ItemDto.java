package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Integer id;
    @NotBlank(message = "не должно быть пустым")
    private String name;
    @NotBlank(message = "не должно быть пустым")
    private String description;
    @NotNull(message = "не должно быть null")
    private Boolean available;
}
