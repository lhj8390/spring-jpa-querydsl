package com.lhj8390.springjpaquerydsl.dto.guild;

import com.lhj8390.springjpaquerydsl.entity.GuildHistoryType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuildHistoryResponseDTO {

    private String name;
    private String username;
    private GuildHistoryType type;
    private LocalDateTime time;
}
