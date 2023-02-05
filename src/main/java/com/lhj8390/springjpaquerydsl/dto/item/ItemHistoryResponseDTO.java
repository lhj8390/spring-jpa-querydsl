package com.lhj8390.springjpaquerydsl.dto.item;

import com.lhj8390.springjpaquerydsl.entity.ItemType;
import com.lhj8390.springjpaquerydsl.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemHistoryResponseDTO {

    private String username;
    private String itemName;
    private ItemType type;
    private LocalDateTime date;

}
