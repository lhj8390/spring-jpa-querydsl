package com.lhj8390.springjpaquerydsl.dto.item;

import com.lhj8390.springjpaquerydsl.entity.Inventory;
import com.lhj8390.springjpaquerydsl.entity.Item;
import com.lhj8390.springjpaquerydsl.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventorySaveDTO {

    private User user;
    private Item item;
    private int amount;

    @Builder
    public InventorySaveDTO(User user, Item item, int amount) {
        this.user = user;
        this.item = item;
        this.amount = amount;
    }

    public Inventory toEntity() {
        return Inventory.builder()
                .item(getItem())
                .user(getUser())
                .amount(getAmount())
                .build();
    }

}
