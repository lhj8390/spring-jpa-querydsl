package com.lhj8390.springjpaquerydsl.dto.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventorySaveRequestDTO {

    private Long userId;
    private Long itemId;
    private int amount;

    @Builder
    public InventorySaveRequestDTO(Long userId, Long itemId, int amount) {
        this.userId = userId;
        this.itemId = itemId;
        this.amount = amount;
    }

}
