package com.lhj8390.springjpaquerydsl.dto.item;

import com.lhj8390.springjpaquerydsl.entity.ItemType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryResponseDTO {

    private String itemName;

    private ItemType itemType;

    private int amount;
}
