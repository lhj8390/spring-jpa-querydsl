package com.lhj8390.springjpaquerydsl.repository;

import com.lhj8390.springjpaquerydsl.dto.item.ItemHistoryResponseDTO;
import com.lhj8390.springjpaquerydsl.dto.item.ItemHistorySearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemHistoryRepositoryCustom {
    Page<ItemHistoryResponseDTO> getAllHistory(ItemHistorySearchDTO dto, Pageable pageable);
}
