package com.lhj8390.springjpaquerydsl.repository;

import com.lhj8390.springjpaquerydsl.entity.Inventory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @EntityGraph(attributePaths = {"item"})
    List<Inventory> findAllByUserId(Long userId);

    Optional<Inventory> findByUserIdAndItemId(Long userId, Long itemId);
}
