package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.BookingEntity;

import javax.persistence.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "items", schema = "public")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Column(name = "item_name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "available", nullable = false)
    private Boolean available;
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
    @Column(name = "request_id")
    private Long requestId;
    @OneToMany
    @JoinColumn(name = "item_id")
    private List<BookingEntity> bookingEntityList;
    @OneToMany
    @JoinColumn(name = "item_id")
    private List<CommentEntity> commentEntityList;
}
