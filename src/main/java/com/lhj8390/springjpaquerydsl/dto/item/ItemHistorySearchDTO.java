package com.lhj8390.springjpaquerydsl.dto.item;

import com.lhj8390.springjpaquerydsl.entity.ItemHistoryType;
import com.lhj8390.springjpaquerydsl.entity.ItemType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemHistorySearchDTO {
    private String itemName;
    private String userName;
    private ItemType itemType;
    private ItemHistoryType historyType;

    @Builder
    public ItemHistorySearchDTO(String itemName, String userName, ItemType itemType, ItemHistoryType historyType) {
        this.itemName = itemName;
        this.userName = userName;
        this.itemType = itemType;
        this.historyType = historyType;
    }
}
