package com.lhj8390.springjpaquerydsl.dto.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryRemoveRequestDTO {

    private Long inventoryId;
    private int amount;

    @Builder
    public InventoryRemoveRequestDTO(Long inventoryId, int amount) {
        this.inventoryId = inventoryId;
        this.amount = amount;
    }
}
