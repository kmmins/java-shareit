package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "users", schema = "public")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
