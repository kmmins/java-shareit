package ru.practicum.shareit.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.LocalDateTime;

public class DateTimeMatcher extends BaseMatcher<LocalDateTime> {

    public DateTimeMatcher(LocalDateTime time) {
        this.time = time;
    }

    private final LocalDateTime time;

    @Override
    public boolean matches(Object o) {
        LocalDateTime parsed = LocalDateTime.parse(o.toString());
        return time.isEqual(parsed);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("Полученное значение не совпадает с ожидаемым %s.", time));
    }
}
