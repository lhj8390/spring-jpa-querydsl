package com.lhj8390.springjpaquerydsl.service;

import com.lhj8390.springjpaquerydsl.dto.item.ItemHistoryResponseDTO;
import com.lhj8390.springjpaquerydsl.dto.item.ItemHistorySearchDTO;
import com.lhj8390.springjpaquerydsl.repository.ItemHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class HistoryService {

    private final ItemHistoryRepository itemHistoryRepository;

    /**
     * 모든 item history 데이터 찾기 with paging
     * @return Page<ItemHistoryResponseDTO>
     */
    public Page<ItemHistoryResponseDTO> getAllItemHistory(ItemHistorySearchDTO dto, Pageable pageable) {
        return itemHistoryRepository.getAllHistory(dto, pageable);
    }

}
