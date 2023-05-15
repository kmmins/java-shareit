package ru.practicum.shareit.util;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.ParameterException;

public class PageHelper {

    public static PageRequest createRequest(int from, int size) {
        if (from < 0) {
            throw new ParameterException("Ошибка с параметром from.");
        }
        if (size <= 0) {
            throw new ParameterException("Ошибка с параметром size.");
        }
        return PageRequest.of(from / size, size);
    }
}
