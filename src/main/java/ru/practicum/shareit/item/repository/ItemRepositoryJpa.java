package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.shareit.item.model.ItemEntity;

import java.util.List;

@EnableJpaRepositories
public interface ItemRepositoryJpa extends JpaRepository<ItemEntity, Long> {

    List<ItemEntity> findAllByOwnerId(Long ownerId);

    @Query("select item " +
            "from ItemEntity as item " +
            "where upper(item.name) like upper(concat('%', ?1, '%')) " +
            "or upper(item.description) like upper(concat('%', ?1, '%')) " +
            "and item.available = true")
    List<ItemEntity> searchByNameLikeOrDescriptionLikeIgnoreCaseAndAvailable(String text);
}
