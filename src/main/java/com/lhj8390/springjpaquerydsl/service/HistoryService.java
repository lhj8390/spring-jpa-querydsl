package com.lhj8390.springjpaquerydsl.service;

import com.lhj8390.springjpaquerydsl.dto.item.ItemHistoryResponseDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class HistoryService {

    public List<ItemHistoryResponseDTO> getAllItemHistory() {
        // TODO: 모든 item history 데이터 찾기 with paging
        return new ArrayList<>();
    }

}
