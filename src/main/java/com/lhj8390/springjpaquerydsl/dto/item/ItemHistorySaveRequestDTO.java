package com.lhj8390.springjpaquerydsl.dto.item;

import com.lhj8390.springjpaquerydsl.entity.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemHistorySaveRequestDTO {
    private Inventory inventory;

    private int amount;

    private ItemHistoryType type;

    @Builder
    public ItemHistorySaveRequestDTO(Inventory inventory, ItemHistoryType type) {
        this.inventory = inventory;
        this.type = type;
    }

    public ItemHistory toEntity() {
        return ItemHistory.builder()
                .user(inventory.getUser())
                .item(inventory.getItem())
                .amount(inventory.getAmount())
                .type(getType())
                .build();
    }
}
