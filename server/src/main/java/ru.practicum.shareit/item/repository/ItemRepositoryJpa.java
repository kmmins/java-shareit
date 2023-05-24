package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.shareit.item.model.ItemEntity;

@EnableJpaRepositories
public interface ItemRepositoryJpa extends JpaRepository<ItemEntity, Long> {

    Page<ItemEntity> findAllByOwnerIdOrderByIdAsc(Long ownerId, PageRequest pageRequest);

    @Query("select item " +
            "from ItemEntity as item " +
            "where upper(item.name) like upper(concat('%', ?1, '%')) " +
            "or upper(item.description) like upper(concat('%', ?1, '%')) " +
            "and item.available = true")
    Page<ItemEntity> searchByNameLikeOrDescriptionLikeIgnoreCaseAndAvailable(String text, PageRequest pageRequest);
}
