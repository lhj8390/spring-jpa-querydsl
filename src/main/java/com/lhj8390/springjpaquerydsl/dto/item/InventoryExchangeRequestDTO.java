package com.lhj8390.springjpaquerydsl.dto.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryExchangeRequestDTO {

    private Long fromUserId;
    private Long toUserId;
    private Long itemId;
    private int amount;

    @Builder
    public InventoryExchangeRequestDTO(Long fromUserId, Long toUserId, Long itemId, int amount) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.itemId = itemId;
        this.amount = amount;
    }
}
