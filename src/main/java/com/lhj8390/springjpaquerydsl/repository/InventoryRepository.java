package com.lhj8390.springjpaquerydsl.repository;

import com.lhj8390.springjpaquerydsl.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
