package com.lhj8390.springjpaquerydsl.dto.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EquipRequestDTO {
    private Long userId;
    private Long itemId;

    @Builder
    public EquipRequestDTO(Long userId, Long itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }
}
