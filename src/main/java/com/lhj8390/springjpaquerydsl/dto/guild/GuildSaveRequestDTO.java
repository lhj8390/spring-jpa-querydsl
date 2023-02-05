package com.lhj8390.springjpaquerydsl.dto.guild;

import com.lhj8390.springjpaquerydsl.entity.Guild;
import com.lhj8390.springjpaquerydsl.entity.GuildHistoryType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuildSaveRequestDTO {
    private String name;
    private GuildHistoryType type;

    @Builder
    public GuildSaveRequestDTO(String name, GuildHistoryType type) {
        this.name = name;
        this.type = type;
    }

}
