package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.shareit.request.model.RequestEntity;

import java.util.List;

@EnableJpaRepositories
public interface RequestRepositoryJpa extends JpaRepository<RequestEntity, Long> {

    List<RequestEntity> findAllByRequestOwnerOrderByCreatedDesc(Long requestOwner);

    Page<RequestEntity> findAllByRequestOwnerIsNotOrderByCreatedDesc(Long requestOwner, PageRequest pageRequest);
}
