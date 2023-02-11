package com.lhj8390.springjpaquerydsl.dto.item;

import com.lhj8390.springjpaquerydsl.entity.ItemType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ItemHistoryResponseDTO {

    private String username;
    private String itemName;
    private ItemType type;
    private LocalDateTime date;
    private String detail;

}
