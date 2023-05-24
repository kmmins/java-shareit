package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
	@NotNull(message = "не должно быть null.")
	private long itemId;
	@FutureOrPresent
	@NotNull(message = "не должно быть null.")
	private LocalDateTime start;
	@Future
	@NotNull(message = "не должно быть null.")
	private LocalDateTime end;
	//private UserRequestDto booker;
	//private ItemRequestDto item;
	//private BookingStatus status;
}
