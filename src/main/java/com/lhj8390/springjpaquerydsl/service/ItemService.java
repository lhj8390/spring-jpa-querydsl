package com.lhj8390.springjpaquerydsl.service;

import com.lhj8390.springjpaquerydsl.dto.item.InventoryResponseDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    public List<InventoryResponseDTO> getInventory() {
        // TODO: 유저에 따른 inventory 데이터 찾기
        return new ArrayList<>();
    }

    public void insertItem() {
        // TODO: 인벤토리에 아이템 넣기 및 Item 이력 추가 (코인 제한)
    }

    public void removeItem() {
        // TODO: 인벤토리에 아이템 삭제 및 Item 이력 추가
    }

    public void exchangeItem() {
        // TODO: 인벤토리 아이템 교환 및 Item 이력 추가
    }

    public void equip() {
        // TODO: 아이템 장착 - 타입 및 인벤토리 아이템 한정
    }


}
