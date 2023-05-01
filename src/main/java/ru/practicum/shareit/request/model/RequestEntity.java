package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class RequestEntity {

    private final Long id;
    private String description;
    private Long userIdWhoRequest;
}
